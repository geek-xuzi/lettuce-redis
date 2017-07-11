package com.xuen.metrics;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import java.util.Comparator;
import java.util.SortedSet;
import org.apache.commons.lang3.ObjectUtils;

/**
 * @author zheng.xu
 * @since 2017-07-11
 */
public class Tags {

    private static final Comparator<Tag> tagComparator = (o1, o2) -> ObjectUtils
            .compare(o1.key, o2.key);

    // 根据 key 排序
    private final SortedSet<Tag> tags = Sets.newTreeSet(tagComparator);

    public void addTag(String key, String value) {
        tags.add(Tag.newTag(key, value));
    }

    @Override
    public String toString() {
        return "Tags{" +
                "tags=" + tags +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tags tags1 = (Tags) o;

        return tagsEquals(tags1.tags, tags);

    }

    private boolean tagsEquals(SortedSet<Tag> leftTag, SortedSet<Tag> rightTag) {
        if (leftTag.size() != rightTag.size()) {
            return false;
        }
        if (leftTag.size() != 0 && rightTag.size() != 0) {
            int code = 0;
            for (Tag tag : leftTag) {
                code ^= tag.hashcode;
            }
            for (Tag tag : rightTag) {
                code ^= tag.hashcode;
            }
            return code == 0;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tags);
    }

    public SortedSet<Tag> getTags() {
        return tags;
    }
}
