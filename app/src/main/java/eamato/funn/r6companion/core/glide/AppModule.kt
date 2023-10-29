package eamato.funn.r6companion.core.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import eamato.funn.r6companion.core.di.ImageOkHttpClient
import okhttp3.OkHttpClient
import java.io.InputStream

@GlideModule
class AppModule : AppGlideModule() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface ImageEntryPoint {
        @ImageOkHttpClient
        fun getImageOkHttpClient(): OkHttpClient
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val client = EntryPointAccessors.fromApplication(
            context,
            ImageEntryPoint::class.java
        ).getImageOkHttpClient()

        val factory = OkHttpUrlLoader.Factory(client)

        registry.replace(GlideUrl::class.java, InputStream::class.java, factory)
    }
}