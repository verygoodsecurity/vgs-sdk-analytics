package com.verygoodsecurity.sdk.analytics.data.repository

import com.verygoodsecurity.sdk.analytics.data.repository.model.Event as Model
import com.verygoodsecurity.sdk.analytics.data.source.remote.dto.Event as DTO

internal class Mapper {

    fun toDTO(model: Model): DTO {
        return DTO(model.params)
    }
}