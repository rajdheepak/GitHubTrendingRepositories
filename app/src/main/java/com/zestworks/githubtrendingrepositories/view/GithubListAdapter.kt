package com.zestworks.githubtrendingrepositories.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zestworks.githubtrendingrepositories.R
import com.zestworks.githubtrendingrepositories.model.GitHubApiResponse
import de.hdodenhof.circleimageview.CircleImageView

class GithubListAdapter(var githubItems: List<GitHubApiResponse>): RecyclerView.Adapter<GithubListAdapter.GithubListholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubListholder {
        val view =
            LayoutInflater.from(parent.context!!)
                .inflate(R.layout.github_list_holder, parent, false)
        return GithubListholder(view)
    }

    override fun getItemCount(): Int {
        return githubItems.size
    }

    fun updateGithubItems(githubItems: List<GitHubApiResponse>) {
        this.githubItems = githubItems
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: GithubListholder, position: Int) {
        val gitHubApiResponse = githubItems[position]
        holder.text.text = gitHubApiResponse.name
        holder.description.text = gitHubApiResponse.description
        holder.lang.text = gitHubApiResponse.language
        holder.star.text = gitHubApiResponse.stars.toString()
        holder.fork.text = gitHubApiResponse.forks.toString()
        holder.url.text = gitHubApiResponse.url
        Picasso.get().load(gitHubApiResponse.avatar)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.imageView)
        holder.holder.setOnClickListener {
            if(holder.bottom.visibility == View.VISIBLE){
                holder.bottom.visibility = View.GONE
            } else {
                holder.bottom.visibility = View.VISIBLE
            }
        }
    }

    inner class GithubListholder(itemView: View): RecyclerView.ViewHolder(itemView){
        val holder = itemView.findViewById<ConstraintLayout>(R.id.holder)
        val text = itemView.findViewById<TextView>(R.id.title)
        val description = itemView.findViewById<TextView>(R.id.description)
        val imageView = itemView.findViewById<CircleImageView>(R.id.author_image)
        val bottom = itemView.findViewById<ConstraintLayout>(R.id.bottom_expand)
        val lang = itemView.findViewById<TextView>(R.id.language_text)
        val star = itemView.findViewById<TextView>(R.id.star_text)
        val fork = itemView.findViewById<TextView>(R.id.fork_text)
        val url = itemView.findViewById<TextView>(R.id.url)
    }
}