package org.test4j.hamcrest.matcher.property;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.test4j.fortest.beans.User;
import org.test4j.hamcrest.matcher.property.PropertyAnyItemMatcher;
import org.test4j.junit.JTester;

import ext.test4j.hamcrest.MatcherAssert;

@SuppressWarnings("unchecked")
public class PropertyAnyItemMatcherTest implements JTester {
    PropertyAnyItemMatcher matcher = new PropertyAnyItemMatcher("first", the.string().regular("\\w+\\d+\\w+"));

    @Test
    public void testMatches_List_HasPropMatch() {
        List<User> users = Arrays.asList(new User("dfasdf", "eedaf"), new User("firs3445tname", "lastname"));
        MatcherAssert.assertThat(users, matcher);
    }

    @Test
    public void testMatches_Array_HasPropMatch() {
        User[] users = new User[] { new User("dfasdf", "eedaf"), new User("firs3445tname", "lastname") };
        MatcherAssert.assertThat(users, matcher);
    }

    @Test(expected = AssertionError.class)
    public void testMatches_List_HasPropNotMatch() {
        List<User> users = Arrays.asList(new User("dfasdf", "eedaf"), new User("eaafsd", "lastname"));
        MatcherAssert.assertThat(users, matcher);
    }
}