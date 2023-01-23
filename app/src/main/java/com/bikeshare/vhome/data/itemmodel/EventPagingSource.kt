package com.bikeshare.vhome.data.itemmodel

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bikeshare.vhome.data.model.EventSearchPost
import com.bikeshare.vhome.data.remote.ApiInterface
import retrofit2.HttpException
import java.io.IOException

private const val EVENT_STARTING_INDEX = 1

class EventPagingSource(
    private val vhomeApi: ApiInterface,
    private val token: String,
    private val post: EventSearchPost
) : PagingSource<Int,EventItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EventItem> {
        val position = params.key ?: EVENT_STARTING_INDEX

        return try {
            post.page = position
            val response = vhomeApi.searchEvent(token,post)
            val photos = response.body()!!.data.events

            LoadResult.Page(
                data = photos,
                prevKey = if (position == EVENT_STARTING_INDEX) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, EventItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}