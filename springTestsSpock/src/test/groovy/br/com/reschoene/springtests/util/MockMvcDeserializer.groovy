package br.com.reschoene.springtests.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.test.web.servlet.ResultActions

class MockMvcDeserializer {
    def static getObjectContent(ResultActions resultActions, Class valueType, ObjectMapper objectMapper){
        def stringContent = resultActions.andReturn().response.contentAsString
        return objectMapper.readValue(stringContent, valueType)
    }
}
