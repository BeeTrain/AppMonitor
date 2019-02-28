package ru.chernakov.appmonitor.presentation.list

import android.os.Bundle
import butterknife.OnClick
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.R
import ru.chernakov.appmonitor.presentation.base.BaseFragment

class ListFragment : BaseFragment(), ListView {

    override val layoutRes = R.layout.fragment_list

    @InjectPresenter
    lateinit var presenter: ListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        App.instance.getAppComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun init() {

    }

    @ProvidePresenter
    fun providePresenter(): ListPresenter {
        return ListPresenter(router)
    }

    @OnClick(R.id.text)
    fun goToNext() {
        presenter.goToInfo()
    }

}