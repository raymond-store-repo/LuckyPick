package com.raymond.drawgenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

/**
 * Created by Raymond on 8/10/2014.
 */
public class SimpleLotteryPicksGenerator implements LotteryPicksGeneratorInterface
{
  private Integer picksCount;
  private Integer poolCount;

  private SimpleLotteryPicksGenerator()
  {
        /* Default to 6/49 */
    picksCount = picksCount == null ? 6 : picksCount;
    poolCount = poolCount == null ? 49 : poolCount;
  }

  public static final SimpleLotteryPicksGenerator createSimpleLotteryPicksGenerator()
  {
    return new SimpleLotteryPicksGenerator();
  }

  @Override
  public ArrayList<Integer> generateNumberPicks()
  {
    HashSet<Integer> set = new HashSet<Integer>(picksCount);

    while (set.size() < picksCount)
    {
      Random randomGenerator = new Random();
      Integer random = randomGenerator.nextInt(poolCount) + 1;
      set.add(random);
    }

    ArrayList<Integer> result = new ArrayList<Integer>(set);

        /* Sort the result */
    Collections.sort(result);

    return result;
  }

  public Integer getPicksCount()
  {
    return picksCount;
  }

  public void setPicksCount(Integer picksCount)
  {
    this.picksCount = picksCount;
  }

  public Integer getPoolCount()
  {
    return poolCount;
  }

  public void setPoolCount(Integer poolCount)
  {
    this.poolCount = poolCount;
  }
}
