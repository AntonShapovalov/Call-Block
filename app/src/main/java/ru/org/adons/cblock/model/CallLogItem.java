package ru.org.adons.cblock.model;

import android.provider.CallLog;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

import ru.org.adons.cblock.data.CallLogModel;

/**
 * Model for {@link CallLog.Calls} item, used in {@link CallLogModel}
 */

@AutoValue
public abstract class CallLogItem {

    public abstract Long id();

    public abstract String phoneNumber();

    public abstract Long date();

    @Nullable
    public abstract String name();

    public static CallLogItem.Builder builder() {
        return new AutoValue_CallLogItem.Builder();
    }

    @SuppressWarnings("WeakerAccess")
    @AutoValue.Builder
    public static abstract class Builder {

        public abstract CallLogItem.Builder setId(Long id);

        public abstract CallLogItem.Builder setPhoneNumber(String phoneNumber);

        public abstract CallLogItem.Builder setDate(Long date);

        public abstract CallLogItem.Builder setName(@Nullable String name);

        public abstract CallLogItem build();
    }

}
