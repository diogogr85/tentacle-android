package br.com.concrete.tentacle.viewmodel

import br.com.concrete.tentacle.base.BaseViewModelTest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.Session
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.features.login.LoginViewModel
import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.standalone.inject

class LoginVMTest : BaseViewModelTest() {

    private val loginViewMock: LoginViewModel by inject()

    @Test
    fun `when LoginViewModel calls login should return error message for 401`() {
        val expected =
            ViewStateModel<Session>(
                status = ViewStateModel.Status.ERROR, model = null, errors = ErrorResponse())
        var actual = ViewStateModel<Session>(status = ViewStateModel.Status.LOADING)

        val mockResponse = MockResponse()
            .setResponseCode(401)

        mockServer.enqueue(mockResponse)

        loginViewMock.getStateModel().observeForever {
            actual = it
        }
        loginViewMock.loginUser("daivid.v.leal@concrete.com.br", "123456")

        var request = mockServer.takeRequest()
        assertEquals("/login", request.path)
        assertEquals(expected, actual)
    }

    @Test
    fun `when LoginViewModel calls login should return error message for 400`() {
        val responseJson = this::class
            .java
            .classLoader?.getResource(
            "mockjson/errors/error_400.json"
        )?.readText()

        val responseObject: ErrorResponse =
            GsonBuilder().create().fromJson(responseJson, ErrorResponse::class.java)

        val expected =
            ViewStateModel<Session>(
                status = ViewStateModel.Status.ERROR, model = null, errors = responseObject)
        var actual = ViewStateModel<Session>(status = ViewStateModel.Status.LOADING)

        val mockResponse = MockResponse()
            .setResponseCode(400)
            .setBody(responseJson)

        mockServer.enqueue(mockResponse)

        loginViewMock.getStateModel().observeForever {
            actual = it
        }
        loginViewMock.loginUser("daivid.v.leal@concrete.com.br", "123456")

        var request = mockServer.takeRequest()
        assertEquals("/login", request.path)
        assertEquals(expected, actual)
    }

    @Test
    fun `when LoginViewModel calls login should return success`() {
        val responseJson = this::class
            .java
            .classLoader?.getResource(
            "mockjson/login/login_success.json"
            )?.readText()

        val collectionType = object : TypeToken<BaseModel<Session>>() {}.type
        val responseObject: BaseModel<Session> =
            GsonBuilder().create().fromJson(responseJson, collectionType)

        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.SUCCESS,
                model = responseObject.data)
        var actual = ViewStateModel<Session>(status = ViewStateModel.Status.LOADING)

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(responseJson)

        mockServer.enqueue(mockResponse)

        loginViewMock.getStateModel().observeForever {
            actual = it
        }
        loginViewMock.loginUser("daivid.v.leal@concrete.com.br", "123456")

        var request = mockServer.takeRequest()
        assertEquals("/login", request.path)
        assertEquals(expected, actual)
    }
}