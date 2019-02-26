package ru.chernakov.appmonitor.presentation.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_list.*
import ru.chernakov.appmonitor.R
import ru.chernakov.appmonitor.presentation.base.BaseFragment

class ListFragment : BaseFragment(), ListView {

    @InjectPresenter
    lateinit var presenter: ListPresenter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val v = inflater.inflate(R.layout.fragment_list, container, false)

        text.setOnClickListener { v1 -> presenter.goTo() }

        return v
    }
}