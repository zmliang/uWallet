package com.self.app.viewmodel

import androidx.lifecycle.MutableLiveData
import com.self.app.ui.view.chart.ChartView.ChartModel
import com.self.base.BaseViewModel
import java.util.*

class HandicapViewModel: BaseViewModel()  {

    //var handicapInfos: MutableLiveData<HandicapInfo> = MutableLiveData()

    var handicapInfos: MutableLiveData<ArrayList<ChartModel>> = MutableLiveData()

    fun reqHandicapInfo(){
        //val volume = 10f
        val list = ArrayList<ChartModel>()
        for (i in 0..5) {
            val model = ChartModel()
            model.volume = (Random().nextInt(9) + 1).toFloat()
            list.add(model)
        }
        handicapInfos.value = list
    }
}