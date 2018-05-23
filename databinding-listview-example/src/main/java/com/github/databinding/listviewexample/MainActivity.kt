package com.github.databinding.listviewexample

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.github.databinding.listviewexample.data.SearchResultAdapter
import com.github.databinding.listviewexample.data.SearchResultViewModel
import com.github.databinding.listviewexample.data.SearchViewModel
import com.github.databinding.listviewexample.data.TextWatcherWrapper
import com.github.databinding.listviewexample.data.UiButtonModel
import com.github.databinding.listviewexample.databinding.ActivityMainBinding
import org.json.JSONObject
import java.lang.IllegalArgumentException
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var executor: ExecutorService

    private val TAG = "DATABINDING_EXAMPLE"

    private var searchString = ""
    private var lastResult: String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("BUNDLE_LAST_SEARCH_RESULT")) {
                lastResult = savedInstanceState.get("BUNDLE_LAST_SEARCH_RESULT").toString()
                val jsonResult = JSONObject(lastResult)
                val uiHandler = Handler()
                updateListViewWithResult(jsonResult, uiHandler)
            }
            if (savedInstanceState.containsKey("BUNDLE_LAST_SEARCH_STRING")) {
                searchString = savedInstanceState.get("BUNDLE_LAST_SEARCH_STRING").toString()
                val searchEditText = findViewById<EditText> (R.id.searchText)
                searchEditText.setText(searchString)
            }
        } else {
            initializeSearchView()
        }
        executor = Executors.newCachedThreadPool()
    }

    override fun onResume() {
        super.onResume()
        registerSearch()
    }

    private fun initializeSearchView() {
        binding.searchViewModel = SearchViewModel()
    }

    /**
     * Updates view using data binding
     */
    private fun registerSearch() {

        val uiHandler = Handler()

        binding.searchViewModel = SearchViewModel(
            searchLabel = getString(R.string.search_label),
            searchText = getString(R.string.search_test),
            textWatcher = TextWatcherWrapper({ stringTosearch -> searchString = stringTosearch }),
            searchButton = UiButtonModel(
                label = getString(R.string.search_button_label),
                click = {
                    Toast.makeText(applicationContext,
                        getString(R.string.searching),
                        Toast.LENGTH_SHORT)
                        .show()
                    fetchDataFromGithubApi(searchString, uiHandler)
                }
            )
        )
    }

    /**
     * Here we perform a network call to GitHub API
     * Here an ExecutorService is used to make the network call, so that it always works in the non UI thread.
     * @param searchString The searchString is used to search the Github Api
     * * If searchString is Empty, we show a Toast to enter valid search string
     * * Any exception from server is handled
     * @param uiHandler is used to communicate with the UI Thread
     */
    private fun fetchDataFromGithubApi(searchString: String, uiHandler: Handler) {
        if (searchString.isEmpty()) {
            Toast.makeText(applicationContext,
                getString(R.string.enter_valid_search_string),
                Toast.LENGTH_SHORT)
                .show()
            return
        }

        executor.execute({
            try {
                // Searching the Github repository for the requested search key and
                // fetching the results ordered by star count
                val result = URL(
                        "https://api.github.com/search/repositories?q=$searchString&sort=stars&order=desc")
                        .readText()
                Log.d(TAG, result)
                val jsonResult = JSONObject(result)
                lastResult = result
                updateListViewWithResult(jsonResult, uiHandler)
            } catch (ex: Exception) {
                Log.d(TAG, "exception returned from server: ${ex.message}")
                uiHandler.post {
                    Toast.makeText(applicationContext,
                        getString(R.string.server_not_available),
                        Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }

    /**
     * Updates List View using data binding
     * @param jsonResult The json data obtained by querying the Github api.
     * * We parse the json data to get information about the url of owner's avatar of a repository,
     * * The name of the github repository
     * * The description of the Github repository
     * * The ★ count, denoting the popularity of the repository
     * @param uiHandler The searchResultAdapter is created using all the necessary data required
     * by the list view and passed to the uiHandler to display on the screen.
     * Note: updateListViewWithResult is called from worker thread so we need to communicate
     * the data to the UI thread using a handler that was created in the UI thread.
     */
    private fun updateListViewWithResult(jsonResult: JSONObject, uiHandler: Handler) {
        try {
            val itemArray = jsonResult.getJSONArray("items")
            if (itemArray.length() < 1) {
                throw IllegalArgumentException("No Item Found")
            }
            val dataList = mutableListOf<SearchResultViewModel>()
            for (i in 0 until itemArray.length() - 1) {
                val currentObject: JSONObject = itemArray.get(i) as JSONObject
                Log.d(TAG, "currentObject $currentObject")
                val ownerAvatar: String = currentObject.getJSONObject("owner").getString("avatar_url")
                val repositoryName: String = currentObject.getString("name")
                val description: String = currentObject.getString("description")
                val numberOfStars: String = "★ : ${currentObject.getString("stargazers_count")}"
                dataList.add(SearchResultViewModel(ownerAvatar, repositoryName, description, numberOfStars))
            }
            val listView: ListView = findViewById(R.id.search_list)
            val searchResultAdapter = SearchResultAdapter(this, dataList)
            uiHandler.post({
                listView.adapter = searchResultAdapter
            })
        } catch (ex: Exception) {
            uiHandler.post({
                Toast.makeText(applicationContext,
                    getString(R.string.result_not_found),
                    Toast.LENGTH_LONG)
                    .show()
            })
        }
    }

    /**
     * Saving last state for run time configuration changes like screen rotation
     */
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if (lastResult != null) {
            outState!!.putSerializable("BUNDLE_LAST_SEARCH_RESULT", lastResult)
        }
        if (searchString != "") {
            outState!!.putSerializable("BUNDLE_LAST_SEARCH_STRING", searchString)
        }
    }
}
