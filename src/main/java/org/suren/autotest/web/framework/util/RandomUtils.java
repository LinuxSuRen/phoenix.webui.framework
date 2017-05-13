/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.suren.autotest.web.framework.util;

import java.util.Random;

/**
 * @author suren
 * @date 2017年1月23日 下午3:08:45
 */
public class RandomUtils
{

    /**
     * Random object used by random method. This has to be not local to the
     * random method so as to not return the same value in the same millisecond.
     */
    private static final Random RANDOM = new Random();

    /**
     * <p>
     * {@code RandomUtils} instances should NOT be constructed in standard
     * programming. Instead, the class should be used as
     * {@code RandomUtils.nextBytes(5);}.
     * </p>
     * 
     * <p>
     * This constructor is public to permit tools that require a JavaBean
     * instance to operate.
     * </p>
     */
    public RandomUtils() {
        super();
    }

    /**
     * <p>
     * Creates an array of random bytes.
     * </p>
     * 
     * @param count
     *            the size of the returned array
     * @return the random byte array
     * @throws IllegalArgumentException if {@code count} is negative
     */
    public static byte[] nextBytes(int count) {
        byte[] result = new byte[count];
        RANDOM.nextBytes(result);
        return result;
    }

    /**
     * <p>
     * Returns a random integer within the specified range.
     * </p>
     * 
     * @param startInclusive
     *            the smallest value that can be returned, must be non-negative
     * @param endExclusive
     *            the upper bound (not included)
     * @throws IllegalArgumentException
     *             if {@code startInclusive > endExclusive} or if
     *             {@code startInclusive} is negative
     * @return the random integer
     */
    public static int nextInt(int startInclusive, int endExclusive) {
        if (startInclusive == endExclusive) {
            return startInclusive;
        }
        
        return startInclusive + RANDOM.nextInt(endExclusive - startInclusive);
    }
    
    public static int nextInt(int n) {
        return nextInt(0, n);
    }
    
    /**
     * <p>
     * Returns a random long within the specified range.
     * </p>
     * 
     * @param startInclusive
     *            the smallest value that can be returned, must be non-negative
     * @param endExclusive
     *            the upper bound (not included)
     * @throws IllegalArgumentException
     *             if {@code startInclusive > endExclusive} or if
     *             {@code startInclusive} is negative
     * @return the random long
     */
    public static long nextLong(long startInclusive, long endExclusive) {
        if (startInclusive == endExclusive) {
            return startInclusive;
        }

        return (long) nextDouble(startInclusive, endExclusive);
    }    
    
    
    /**
     * <p> 
     * Returns a random double within the specified range.
     * </p>
     * 
     * @param startInclusive
     *            the smallest value that can be returned, must be non-negative
     * @param endInclusive
     *            the upper bound (included)
     * @throws IllegalArgumentException
     *             if {@code startInclusive > endInclusive} or if
     *             {@code startInclusive} is negative
     * @return the random double
     */
    public static double nextDouble(double startInclusive, double endInclusive) {
        if (startInclusive == endInclusive) {
            return startInclusive;
        }
        
        return startInclusive + ((endInclusive - startInclusive) * RANDOM.nextDouble());
    }
    
    /**
     * <p>
     * Returns a random float within the specified range.
     * </p>
     * 
     * @param startInclusive
     *            the smallest value that can be returned, must be non-negative
     * @param endInclusive
     *            the upper bound (included)
     * @throws IllegalArgumentException
     *             if {@code startInclusive > endInclusive} or if
     *             {@code startInclusive} is negative
     * @return the random float
     */
    public static float nextFloat(float startInclusive, float endInclusive) {
        if (startInclusive == endInclusive) {
            return startInclusive;
        }
        
        return startInclusive + ((endInclusive - startInclusive) * RANDOM.nextFloat());
    }

}
