package com.learning.mobile.miwok

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.learning.mobile.miwok.databinding.ListItemBinding

// Here, we initialize the ArrayAdapter's internal storage for the context and the list.
// the second argument is used when the ArrayAdapter is populating a single TextView.
// Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
// going to use this second argument, so it can be any value. Here, we used 0.
class WordAdapter(context: Context, wordlist: MutableList<Word>, bgColour: Int) : ArrayAdapter<Word>(context, 0, wordlist) {
    lateinit var binding: ListItemBinding
    var mBgcolor: Int = 0
    init{
        this.mBgcolor = bgColour
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemview : View = convertView
                ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        binding = ListItemBinding.bind(listItemview)
        var word: Word? = getItem(position)
        if (word!!.imgResourceId !=0){
            binding.imagTranslation.setImageResource(word!!.imgResourceId)
            binding.imagTranslation.visibility = View.VISIBLE
        }else{
            binding.imagTranslation.visibility = View.GONE
        }
        //setting background for the vertical layout that contains 2 text viewss
        binding.textContainer.setBackgroundColor(mBgcolor)
        binding.tvMiwokTranslation.text = word!!.miwokTranslation
        binding.tvDefaultTranslation.text = word!!.defaultTranslation

        return listItemview
    }
}