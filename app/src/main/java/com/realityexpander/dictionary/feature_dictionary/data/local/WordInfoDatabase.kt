package com.realityexpander.dictionary.feature_dictionary.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.realityexpander.dictionary.feature_dictionary.data.local.entity.WordInfoEntity

@Database(
    entities = [WordInfoEntity::class],
    version = 1
)
@TypeConverters(Converters::class) // for List<Meaning>
abstract class WordInfoDatabase: RoomDatabase() {

    abstract val dao: WordInfoDao
}