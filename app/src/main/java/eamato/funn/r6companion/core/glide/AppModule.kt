package eamato.funn.r6companion.core.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import eamato.funn.r6companion.core.okhttp.getImageOkHttpClient
import java.io.InputStream

@GlideModule
class AppModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val client = getImageOkHttpClient(context)

        val factory = OkHttpUrlLoader.Factory(client)

        registry.replace(GlideUrl::class.java, InputStream::class.java, factory)
    }
}