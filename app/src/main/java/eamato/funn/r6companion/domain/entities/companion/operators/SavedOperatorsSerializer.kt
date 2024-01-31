package eamato.funn.r6companion.domain.entities.companion.operators

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import eamato.funn.r6companion.SavedOperators
import java.io.InputStream
import java.io.OutputStream

object SavedOperatorsSerializer : Serializer<SavedOperators> {

    override val defaultValue: SavedOperators = SavedOperators.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): SavedOperators {
        try {
            return SavedOperators.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Can't read proto", e)
        }
    }

    override suspend fun writeTo(t: SavedOperators, output: OutputStream) {
        t.writeTo(output)
    }
}