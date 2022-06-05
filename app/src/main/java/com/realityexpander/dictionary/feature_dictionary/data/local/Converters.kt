package com.realityexpander.dictionary.feature_dictionary.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.realityexpander.dictionary.feature_dictionary.data.util.JsonParserInterface
import com.realityexpander.dictionary.feature_dictionary.domain.model.Meaning

@ProvidedTypeConverter  // Required for Room to work
class Converters(
    private val jsonParser: JsonParserInterface  // Allows to use Gson or Jackson or Moshi or another library
) {
    @TypeConverter  // Required for Room to work
    fun fromMeaningsJson(json: String): List<Meaning> {
        return jsonParser.fromJson<ArrayList<Meaning>>(
            json,
            object : TypeToken<ArrayList<Meaning>>(){}.type  // defines the type of the list (yes, ugly)
        ) ?: emptyList()
    }

    @TypeConverter  // Required for Room to work
    fun toMeaningsJson(meanings: List<Meaning>): String {
        return jsonParser.toJson(
            meanings,
            object : TypeToken<ArrayList<Meaning>>(){}.type  // defines the type of the list (yes, ugly)
        ) ?: "[]"
    }
}