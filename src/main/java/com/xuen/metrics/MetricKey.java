package com.xuen.metrics;

import com.google.common.base.CharMatcher;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author zheng.xu
 * @since 2017-07-11
 */
public class MetricKey {

    private static final CharMatcher pointMatcher = CharMatcher.is('.');

    // 指标名
    final String name;
    String host;
    String alias;

    // 标签
    final Tags tags = new Tags();
    private static final Interner<String> interner = Interners.newStrongInterner();


    public MetricKey(String name) {
        this.name = interner.intern(name);
    }

    public MetricKey tag(String key, String value){
        tags.addTag(interner.intern(normalize(key)),interner.intern(normalize(value)));
        return this;
    }

    private static Pattern normalizer = Pattern.compile("[^0-9a-zA-Z_]");
    public static String normalize(String s) {
        if (Strings.isNullOrEmpty(s)) return s;
        return normalizer.matcher(s).replaceAll("_");
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, host, tags);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        MetricKey key = (MetricKey) obj;
        return Objects.equal(this.name, key.name) &&
                Objects.equal(this.tags, key.tags) &&
                Objects.equal(this.host, key.host);
    }

    @Override
    public String toString() {
        return name + tags;
    }

    // 将 "." 替换为下划线
    public static String stripPoint(String hostname) {
        return pointMatcher.replaceFrom(hostname, '_');
    }

    public String getName() {
        return name;
    }

    public MetricKey host(String host) {
        this.host = interner.intern(host);
        return this;
    }

    public MetricKey alias(String alias) {
        this.alias = interner.intern(alias);
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public String getHost() {
        return host;
    }

    public Tags getTags() {
        return tags;
    }

    public Map<String, String> tagMap() {
        HashMap<String, String> ret = Maps.newHashMap();
        for (Tag tag : tags.getTags()) {
            ret.put(tag.key, tag.value);
        }
        return ret;
    }
}
