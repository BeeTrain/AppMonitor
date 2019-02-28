package ru.chernakov.appmonitor.presentation.info

import android.os.Bundle
import butterknife.OnClick
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import ru.chernakov.appmonitor.App
import ru.chernakov.appmonitor.R
import ru.chernakov.appmonitor.presentation.base.BaseFragment

class InfoFragment : BaseFragment(), InfoView {

    override val layoutRes = R.layout.fragment_info

    @InjectPresenter
    lateinit var presenter: InfoPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        App.instance.getAppComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun init() {

    }

    @ProvidePresenter
    fun providePresenter(): InfoPresenter {
        return InfoPresenter(router)
    }

    @OnClick(R.id.text)
    fun goToNext() {
        presenter.goToList()
    }

}