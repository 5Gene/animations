package spark.layoutchange

import android.view.ViewGroup
import android.widget.Button

/**
 * @author yun.
 * @date 2022/6/9
 * @des [一句话描述]
 * @since [https://github.com/5hmlA]
 * <p><a href="https://github.com/5hmlA">github</a>
 */
object Worker {
    fun addView(parent: ViewGroup) {
        parent.addView(newButton(parent))
    }

    fun newButton(parent: ViewGroup):Button {
        return Button(parent.context).apply {
            text = parent.childCount.toString()
            setOnClickListener {
                parent.removeView(this)
            }
        }
    }
}