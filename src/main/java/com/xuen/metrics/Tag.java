package com.xuen.metrics;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.hash.Hashing;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zheng.xu
 * @since 2017-07-11
 */
public class Tag implements Comparable<Tag> {

    public static final String SPLITER = "|";
    public final String key;
    public final String value;
    public final int hashcode;

    private static final ConcurrentHashMap<String, Tag> tagCache = new ConcurrentHashMap<>();

    public Tag(String key, String value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        this.key = nomalizeTagKey(key);
        this.value = value;
        this.hashcode = Hashing.md5().hashString(this.key, Charsets.UTF_8).asInt() ^
                Hashing.md5().hashString(this.value, Charsets.UTF_8).asInt();
    }


    public static Tag newTag(String key, String value) {
        Tag tag = new Tag(key, value);
        Tag old = tagCache.putIfAbsent(key + SPLITER + value, tag);
        return old == null ? tag : old;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null && getClass() != o.getClass()) {
            return false;
        }

        Tag tag = (Tag) o;
        return Objects.equal(this.key, tag.key) && Objects.equal(this.value, tag.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key, value);
    }

    @Override
    public int compareTo(Tag o) {
        return this.key.compareTo(o.key);
    }

    private String nomalizeTagKey(String key) {
        if (PrometheusConstants.isReserved(key)) {
            return "_" + key + "_";
        }
        return key;
    }

}
