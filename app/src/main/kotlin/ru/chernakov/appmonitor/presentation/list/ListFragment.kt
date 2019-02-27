package ru.chernakov.appmonitor.presentation.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import ru.chernakov.appmonitor.R
import ru.chernakov.appmonitor.presentation.AppActivity
import ru.chernakov.appmonitor.presentation.base.BaseFragment

class ListFragment : BaseFragment(), ListView {

    @InjectPresenter
    lateinit var presenter: ListPresenter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val v = inflater.inflate(R.layout.fragment_list, container, false)

        val text = v.findViewById<TextView>(R.id.text)

        text.setOnClickListener { presenter.goTo() }

        return v
    }

    @ProvidePresenter
    fun providePresenter(): ListPresenter {
        return ListPresenter(activity as AppActivity)
    }
}