package com.bikeshare.vhome.ui.cameraevent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bikeshare.vhome.data.UserPreferences
import com.bikeshare.vhome.data.itemmodel.CameraItem
import com.bikeshare.vhome.data.itemmodel.EventItem
import com.bikeshare.vhome.data.model.EventSearchPost
import com.bikeshare.vhome.databinding.DialogBottomBinding
import com.bikeshare.vhome.databinding.FragmentEventBinding
import com.bikeshare.vhome.ui.adapter.CameraAdapter
import com.bikeshare.vhome.ui.adapter.EventAdapter
import com.bikeshare.vhome.ui.adapter.EventListAdapter
import com.bikeshare.vhome.ui.dialog.MyCalendar
import com.bikeshare.vhome.ui.dialog.MyDialog
import com.bikeshare.vhome.util.QUERY_PAGE_SIZE
import com.bikeshare.vhome.util.Resource
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class EventFragment : Fragment(), EventAdapter.OnItemClickListener {
    private val viewModel: EventViewModel by viewModels()

    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!
    private var _bindingDialog: DialogBottomBinding? = null
    private val bindingDialog get() = _bindingDialog!!

    private var calendar = MyCalendar()

    private lateinit var cameraAdapter: CameraAdapter
    private var cameraItems: ArrayList<CameraItem> = ArrayList()

    private lateinit var eventAdapter: EventAdapter

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentEventBinding.inflate(inflater, container, false)
        _bindingDialog = DialogBottomBinding.inflate(inflater, container, false)
        val view = binding.root

        /**
         * 1. Access Datastore CameraState = null
         * 2. Add Data 1 vào DataStore
         * 3. DataStore LiveData access data CamState 1
         * 4. Đưa data CamState 1 vào khay để hiển thị Dialog, đồng thời searchEvent (data CamState 1)
         * 5. Nếu mở Dialog thay đổi thành data 2
         * 6. DataStore LiveData access data CamState 2
         * 7. Đưa data CamState 2 vào khay để hiển thị Dialog, đồng thời searchEvent (data CamState 2)
         * 8. Lặp lại...
         */
        /*val userPreferences = UserPreferences(requireContext())
        userPreferences.accessCameraState.asLiveData().observe(viewLifecycleOwner, Observer {
            Log.i("LOG_CAMSTATE", it.toString())
            if (it.isNullOrEmpty()) {
                //Add Data 1 vào DataStore
                addNewCameraItems()
            } else {
                // Đưa data CamState vào khay để hiển thị Dialog
                cameraItems.clear()
                val jsonArray = JSONArray(it.toString())
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = JSONObject(jsonArray[i].toString())
                    cameraItems.add(CameraItem(jsonObject.getString("cameraName"), jsonObject.getBoolean("isChecked")))
                }

            }
        })

        userPreferences.accessCameraEvent.asLiveData().observe(viewLifecycleOwner, Observer {
            userPreferences.accessDate.asLiveData().observe(viewLifecycleOwner, Observer {
                //Gọi API Search Event
                searchEvent()
            })
        })*/

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //addNewCameraItems()
        addEventItems()

        binding.apply {
            selectDay.apply {
                setText(calendar.getDate())
                setOnClickListener {
                    showCalendarDialog()
                }
            }
            selectEvent.setOnClickListener {
                showEventDialog()
            }
            selectType.setOnClickListener {
                showTypeDialog()
            }
            selectCamera.setOnClickListener {
                showListCamDialog()
            }
            buttonSearchEvent.isGone = true
            /*buttonSearchEvent.setOnClickListener {
                searchEvent()
            }*/
        }
    }

    private fun showCalendarDialog() {
        calendar.show(parentFragmentManager, "CALENDAR")
        calendar.onCalendarOkClick = { year, month, day ->
            calendar.setDate(year, month, day)
            binding.selectDay.setText(calendar.getDate())
            searchEvent()
        }
    }

    private fun showEventDialog() {
        val dialog = MyDialog()
        dialog.show(parentFragmentManager, MyDialog.EVENT_DIALOG)
        dialog.onClickItemEvent = {
            lifecycleScope.launch {
                viewModel.saveCameraEvent(it)
                if (it == 0) {
                    binding.selectEvent.setText("Tất cả")
                    bindingDialog.radioButton1.isChecked = true
                }
                else if (it == 17) {
                    binding.selectEvent.setText("Phát hiện chuyển động")
                    bindingDialog.radioButton2.isChecked = true
                }
                else if (it == 6) {
                    binding.selectEvent.setText("Phát hiện người")
                    bindingDialog.radioButton3.isChecked = true
                }
                else binding.selectEvent.setText("Tất cả")
            }
        }
        UserPreferences(requireContext()).accessToken.asLiveData().observe(viewLifecycleOwner, Observer {
            searchEvent()
        })

    }

    private fun showTypeDialog() {
        val dialog = MyDialog()
        dialog.show(parentFragmentManager, MyDialog.TYPE_DIALOG)
        dialog.onClickItem = {
            lifecycleScope.launch {
                viewModel.saveCameraType(it)
                binding.selectType.setText(it)
            }
        }
    }

    private fun showListCamDialog() {
        /**Cài đặt Dialog, RecyclerView và Adapter để hiển thị item*/
        cameraAdapter = CameraAdapter(cameraItems!!)
        val dialog = MyDialog(cameraAdapter)
        dialog.show(parentFragmentManager, MyDialog.CAMERA_DIALOG)

        cameraAdapter.onItemClick = {
            updateCameraItem(it.getInt("position"),it.getBoolean("isChecked"))

            /*val jsonArray = JsonArray()
            for (i in 0 until it.size) {
                val jsonObject = JsonObject()
                jsonObject.addProperty("cameraName", it[i].cameraName)
                jsonObject.addProperty("isChecked", it[i].isChecked)
                jsonArray.add(jsonObject)
            }
            Log.i("LOG_STATE_SAVE", jsonArray.toString())
            lifecycleScope.launch {
                viewModel.saveCameraState(jsonArray.toString())
            }*/
        }

    }

    private fun updateCameraItem(position: Int, isChecked: Boolean) {
        cameraItems.get(position).isChecked = isChecked
        cameraAdapter.notifyItemChanged(position)

        /**Save State*/
        val jsonArray = JsonArray()
        for (i in 0 until cameraItems.size) {
            val jsonObject = JsonObject()
            jsonObject.addProperty("cameraName", cameraItems[i].cameraName)
            jsonObject.addProperty("isChecked", cameraItems[i].isChecked)
            jsonArray.add(jsonObject)
        }
        lifecycleScope.launch {
            viewModel.saveCameraState(jsonArray.toString())
            /** Reload  Fragment */
        }
    }

    private fun addNewCameraItems() {
        /** Call API Get List Cam*/
        val userPreferences = UserPreferences(requireContext())
        lifecycleScope.launch {
            //Log.i("LOG_userPreferences", userPreferences.accessTokenString()!!)
            viewModel.getListCam(userPreferences.accessTokenString()!!)
        }
        viewModel.getListCamResponseLiveData.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                when (it) {
                    is Resource.Success -> {
                        Log.d("GETLISTDEVICE_OBSERVER", it.data!!.data.toString())
                        val jsonArrayData = it.data!!.data
                        /*val jsonArrayDatastore = JsonArray()

                        //Add checkbox "Tất cả"
                        val jsonObjectDatastoreAll = JsonObject()
                        jsonObjectDatastoreAll.addProperty("cameraName", "Tất cả")
                        jsonObjectDatastoreAll.addProperty("isChecked", true)
                        jsonArrayDatastore.add(jsonObjectDatastoreAll)

                        //Add checkbox lấy từ Server
                        for (i in 0 until jsonArrayData!!.size()) {
                            val jsonObject = JSONObject(jsonArrayData[i].toString())
                            val jsonObjectDatastore = JsonObject()
                            jsonObjectDatastore.addProperty("cameraName", jsonObject.getString("deviceId"))
                            jsonObjectDatastore.addProperty("isChecked", true)
                            jsonArrayDatastore.add(jsonObjectDatastore)
                        }

                        //Lưu dữ liệu vào datastore
                        if (handleSaveCamState == false) {
                            lifecycleScope.launch {
                                viewModel.saveCameraState(jsonArrayDatastore.toString())
                                Log.i("LOG_STATE_SAVE", jsonArrayDatastore.toString())
                                handleSaveCamState = true
                            }
                        }*/

                        /** Add cameraItems*/
                        cameraItems.clear()
                        for (i in 0 until jsonArrayData!!.size()) {
                            val jsonObject = JSONObject(jsonArrayData[i].toString())
                            cameraItems.add(CameraItem(jsonObject.getString("deviceId"), true))
                        }
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), "Get List Device Failure", Toast.LENGTH_LONG).show()
                        Log.e("GETLISTDEVICE_OBSERVER_ERROR", it.data.toString())
                    }
                }
            }
        })
    }

    private fun addEventItems() {
        searchEvent()
        eventAdapter = EventAdapter(this)
        binding.apply {
            recyclerViewEvent.apply {
                setHasFixedSize(true)
                itemAnimator = null
                adapter = eventAdapter
                Log.d("Adapter", adapter.toString())
            }
        }
    }

    /** Call API Search Event */
    private fun searchEvent() {
        val userPreferences = UserPreferences(requireContext())
        lifecycleScope.launch {
            val token = userPreferences.accessTokenString()!!
            val timestamp = SimpleDateFormat("dd/MM/yyyy").parse(calendar.getDate()).time
            val startTime = (timestamp + 1)
            val endTime = (timestamp + 86400000)
            val cameraEvent = userPreferences.accessCameraEventInt()
            /*var deviceIds = arrayListOf<String>()
            val jsonArray = JSONArray(userPreferences.accessCameraStateString())
            for (i in 0 until jsonArray.length()) {
                val jsonObject = JSONObject(jsonArray[i].toString())
                if (jsonObject.getBoolean("isChecked") == true) {
                    deviceIds.add(jsonObject.getString("cameraName"))
                }
            }*/
            val post = EventSearchPost(startTime, endTime, arrayListOf("CNME10000454","CNME00000305"),
                cameraEvent, "", 1, 1, QUERY_PAGE_SIZE, false)
            Log.d("RETROFIT_SEARCHEVENT", "sent")
            //1670432400001,1670518800000
            //startTime, endTime
            viewModel.getSearchEvent(token,post).observe(viewLifecycleOwner){
                it?.let {
                    eventAdapter.submitData(lifecycle,it)
                }
            }
        }
    }

    override fun onItemClick(event: EventItem) {
        Toast.makeText(requireContext(),"${event.deviceId}  " +
                "${SimpleDateFormat("HH:mm:ss").format(Date(event.createDate))}",Toast.LENGTH_SHORT).show()
    }

    /*private fun addEventItemsListAdapter() {
        searchEvent()
        val eventAdapter = EventListAdapter(this)

        binding.apply {
            recyclerViewEvent.apply {
                adapter = eventAdapter
                setHasFixedSize(true)
                addOnScrollListener(this@EventFragment.scrollListener)
            }
        }

        viewModel.searchEventResponseLiveData.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                when(it) {
                    is Resource.Success -> {
                        isLoading = false
                        it.data!!.data.let {eventData ->
                            Log.d("SEARCHEVENT_OBSERVER", eventData.events.toList().toString())
                            eventAdapter.submitList(eventData.events.toList())
                            val totalPages = eventData.totalEvents / QUERY_PAGE_SIZE + 2
                            isLastPage = QUERY_PAGE == totalPages
                            if(isLastPage) binding.recyclerViewEvent.setPadding(0,0,0,0)
                        }
                    }
                    is Resource.Error -> {
                        isLoading = true
                        Toast.makeText(requireContext(), "Search Event Failure", Toast.LENGTH_LONG).show()
                        Log.e("SEARCHEVENT_OBSERVER_ERROR", it.data.toString())
                    }
                }
            }
        })
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){ //State is scrolling
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val totalVisibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + totalVisibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if(shouldPaginate){
                QUERY_PAGE++
                searchEvent()
                isScrolling = false
            }
        }
    }*/
}