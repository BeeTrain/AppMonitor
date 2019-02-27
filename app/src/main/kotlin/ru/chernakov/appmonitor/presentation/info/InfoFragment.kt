package ru.chernakov.appmonitor.presentation.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import ru.chernakov.appmonitor.R
import ru.chernakov.appmonitor.presentation.AppActivity
import ru.chernakov.appmonitor.presentation.base.BaseFragment

class InfoFragment : BaseFragment(), InfoView {

    @InjectPresenter
    lateinit var presenter: InfoPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val v = inflater.inflate(R.layout.fragment_info, container, false)

        return v
    }

    @ProvidePresenter
    fun providePresenter(): InfoPresenter {
        return InfoPresenter(activity as AppActivity)
    }

}