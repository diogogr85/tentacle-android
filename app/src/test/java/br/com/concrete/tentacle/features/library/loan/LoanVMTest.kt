package br.com.concrete.tentacle.features.library.loan

import br.com.concrete.tentacle.base.BaseViewModelTest
import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.ErrorResponse
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.models.library.Library
import br.com.concrete.tentacle.features.library.LoanViewModel
import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert
import org.junit.Test
import org.koin.standalone.inject

class LoanVMTest : BaseViewModelTest() {

    private val loanViewModel: LoanViewModel by inject()

    @Test
    fun `when loanViewModel calls getLibrary should return a Library`() {

        val responseJson = getJson(
            "mockjson/library/get_one_library_success.json"
        )

        val collectionType = object : TypeToken<BaseModel<Library>>() {}.type
        val responseObject: BaseModel<Library> = GsonBuilder()
            .create()
            .fromJson(responseJson, collectionType)

        val expected =
            ViewStateModel(
                status = ViewStateModel.Status.SUCCESS,
                model = responseObject.data)

        var actual = ViewStateModel<Library>(status = ViewStateModel.Status.LOADING)

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(responseJson)

        mockServer.enqueue(mockResponse)

        loanViewModel.getLibrary().observeForever {
            actual = it
        }

        loanViewModel.loadLibrary("someId")
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when loanViewModel calls loadLibrary should return error message for 401`() {
        val expected =
            ViewStateModel<Library>(
                status = ViewStateModel.Status.ERROR, model = null, errors = ErrorResponse()
            )
        var actual = ViewStateModel<Library>(status = ViewStateModel.Status.LOADING)

        val mockResponse = MockResponse()
            .setResponseCode(401)

        mockServer.enqueue(mockResponse)

        loanViewModel.getLibrary().observeForever {
            actual = it
        }
        loanViewModel.loadLibrary("someId")
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when loanViewModel calls loadLibrary should return error message for 400`() {
        val responseJson = getJson(
            "mockjson/errors/error_400.json"
        )

        val responseObject: ErrorResponse =
            GsonBuilder().create().fromJson(responseJson, ErrorResponse::class.java)

        val expected =
            ViewStateModel<Library>(
                status = ViewStateModel.Status.ERROR, model = null, errors = responseObject)
        var actual = ViewStateModel<Library>(status = ViewStateModel.Status.LOADING)

        mockResponseError400()

        loanViewModel.getLibrary().observeForever {
            actual = it
        }
        loanViewModel.loadLibrary("someId")
        Assert.assertEquals(expected, actual)
    }
}
