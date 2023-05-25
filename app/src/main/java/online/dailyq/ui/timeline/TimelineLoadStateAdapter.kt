package online.dailyq.ui.timeline

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import online.dailyq.databinding.ItemTimelineLoadStateBinding

//로딩 상태를 표시하기 위해 LoadStateAdpater를 상속
//LoadStateAdapter는 항상 하나의 아이템만 갖기 때문에 onCreateViewHolder()와 onBindViewHolder()에서
//뷰 타입과 포지션을 전달하지 않고 LoadState만 전달함
class TimelineLoadStateAdapter(val retry: () -> Unit) :
LoadStateAdapter<TimelineLoadStateViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): TimelineLoadStateViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewHolder = ItemTimelineLoadStateBinding.
                inflate(layoutInflater, parent, false)
        return TimelineLoadStateViewHolder(viewHolder, retry)
    }

    override fun onBindViewHolder(holder: TimelineLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
}