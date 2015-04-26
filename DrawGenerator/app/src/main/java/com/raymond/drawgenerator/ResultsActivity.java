package com.raymond.drawgenerator;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raymond on 8/10/2014.
 */
public class ResultsActivity extends Activity
{
  private Integer pick;
  private Integer pool;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_results);

    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);

    Bundle extras = getIntent().getExtras();
    Bundle mainData = (Bundle) extras.get("main_data");

    pick = mainData.getInt("pick");
    pool = mainData.getInt("pool");

    ArrayList<Integer> results = mainData.getIntegerArrayList("results");

    TextView headerTextView = (TextView) findViewById(R.id.resultsHeaderTextView);
    Resources res = getResources();
    headerTextView.setText(String.format(
        res.getString(R.string.results_header_text), pick, pool));
    showResult(results);
  }

  public void regenerateButtonClick(View view)
  {
    SimpleLotteryPicksGenerator generator
        = SimpleLotteryPicksGenerator.createSimpleLotteryPicksGenerator();

    ArrayList<Integer> picks = generator.generateNumberPicks();
    showResult(picks);
  }

  private void showResult(List<Integer> picks)
  {
    StringBuilder sb = new StringBuilder();
    for (Integer result : picks)
    {
      sb.append(result + " ");
    }

    TextView resultsTextView = (TextView) findViewById(R.id.resultsTextView);
    resultsTextView.setText(sb.toString());
  }

  public boolean onOptionsItemSelected(MenuItem item)
  {
    Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
    startActivityForResult(myIntent, 0);
    return true;

  }
}
