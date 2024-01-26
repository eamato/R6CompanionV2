package eamato.funn.r6companion.domain.entities.companion.operators

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import eamato.funn.r6companion.SavedOperator
import java.io.InputStream
import java.io.OutputStream

object SavedOperatorSerializer : Serializer<SavedOperator> {

    override val defaultValue: SavedOperator = SavedOperator.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): SavedOperator {
        try {
            return SavedOperator.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Can't read proto", e)
        }
    }

    override suspend fun writeTo(t: SavedOperator, output: OutputStream) {
        t.writeTo(output)
    }
}