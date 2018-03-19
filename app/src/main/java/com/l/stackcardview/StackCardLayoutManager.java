package com.l.stackcardview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by L on 2018/3/15.
 * Description:
 */

public class StackCardLayoutManager extends RecyclerView.LayoutManager {

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 在这个方法里面给子item布局
     *
     * @param recycler
     * @param state
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //在布局之前，将所有的子View先Detach掉，放入到Scrap缓存中
        detachAndScrapAttachedViews(recycler);

        //拿到总item数量
        int itemCount = getItemCount();
        if (itemCount < 1) {//没有item当然就没必要布局了
            return;
        }
        int bottomPosition;  //用来记录最底层view的postion

        if (itemCount < StackCardConfig.MAX_SHOW_COUNT) {  //如果不足最大数量(4个)
            //那么最底层就是最后一条数据对应的position
            bottomPosition = itemCount - 1;
        } else {
            //否则最底层就是第MAX_SHOW_COUNT(4)条数据对应的position
            bottomPosition = StackCardConfig.MAX_SHOW_COUNT - 1;
        }

        /*
          这里开始布局且绘制子view
          注意:这里要先从最底层开始绘制,因为后绘制的才能覆盖先绘制的，
              滑动的时候是滑最上面一层的,也就是后绘制的
          position也是层数
         */
        for (int position = bottomPosition; position >= 0; position--) {
            //根据position找recycler要itemview
            View view = recycler.getViewForPosition(position);
            //将子View添加至RecyclerView中
            addView(view);
            //测量子view并且把Margin也作为子控件的一部分
            measureChildWithMargins(view, 0, 0);
            //宽度空隙 getWidth()得到Recycler控件的宽度,getDecoratedMeasuredWidth(view)拿到子view的宽度
            int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
            //高度空隙
            int heightSpace = getHeight() - getDecoratedMeasuredHeight(view);
            //给子view布局,这里居中了
            layoutDecoratedWithMargins(view, widthSpace / 2, 200,
                    widthSpace / 2 + getDecoratedMeasuredWidth(view),
                    getDecoratedMeasuredHeight(view) + 200);

            /*
              下面要调整每一层itemview的的大小及Y轴和Z轴的偏移
              最上面一层（第0层）的Scale为1,translationY为0
              依次往下,每层比上面一层:
              (1)Scale相差0.05f
              (2)translationY相差7dp
              (3)translationZ相差1dp

              注意:最后一层,除了水平方向的大小其他都与上一层一样，所以要特殊判断
             */
            if (position > 0) {  //大于0就是不是最上面那层
                //依次往下，每层都要水平方向缩小
                view.setScaleX(1 - StackCardConfig.SCALE_GAP * position);
                if (position < StackCardConfig.MAX_SHOW_COUNT - 1) {
                    //如果，不是最后一层，就都要调整
                    view.setScaleY(1 - StackCardConfig.SCALE_GAP * position); //垂直方向缩小
                    view.setTranslationY(StackCardConfig.TRANS_Y_GAP * position); //向下平移
                    view.setTranslationZ(StackCardConfig.TRANS_Z_GAP * (StackCardConfig.MAX_SHOW_COUNT - 1 - position));  //Z轴方向的平移
                } else {
                    //否则，就是最后一层，与上一层保持一致
                    view.setScaleY(1 - StackCardConfig.SCALE_GAP * (position - 1)); //垂直方向缩小
                    view.setTranslationY(StackCardConfig.TRANS_Y_GAP * (position - 1)); //向下平移
                    view.setTranslationZ(StackCardConfig.TRANS_Z_GAP * (StackCardConfig.MAX_SHOW_COUNT - 1 - (position - 1)));  //Z轴方向的平移
                }
            } else {
                //否则，是第0层(最上面那层),只需调整Z轴高度
                view.setTranslationZ(StackCardConfig.TRANS_Z_GAP * (StackCardConfig.MAX_SHOW_COUNT - 1));  //Z轴方向的平移
            }
        }
    }
}
