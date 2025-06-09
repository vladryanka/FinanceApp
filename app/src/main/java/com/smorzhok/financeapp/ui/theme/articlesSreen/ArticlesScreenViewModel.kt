package com.smorzhok.financeapp.ui.theme.articlesSreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.domain.Articles

class ArticlesScreenViewModel: ViewModel() {
    private val initialArticlesList = mutableListOf<Articles>()
        .apply {
            repeat(15) {
                add(
                    Articles(
                        id = it,
                        iconLeadingResId = R.drawable.emoji_placeholder,
                        textLeadingResId = R.string.products_placeholder,
                        iconTrailingResId = R.drawable.more_vert_icon,
                    )
                )
            }
        }

    private val _articlesList= MutableLiveData<List<Articles>>(initialArticlesList)
    val articlesList: LiveData<List<Articles>> get() = _articlesList

}