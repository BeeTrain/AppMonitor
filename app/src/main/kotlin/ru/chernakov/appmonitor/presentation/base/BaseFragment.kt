package ru.chernakov.appmonitor.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.terrakok.cicerone.Router
import javax.inject.Inject


abstract class BaseFragment : MvpAppCompatFragment() {

    abstract val layoutRes: Int

    private lateinit var unBinder: Unbinder

    @Inject
    lateinit var router: Router

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val v = inflater.inflate(layoutRes, container, false)
        unBinder = ButterKnife.bind(this, v)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unBinder.unbind()
    }

    abstract fun loadData()
}