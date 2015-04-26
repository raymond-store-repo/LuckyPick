package com.raymond.drawgenerator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends Activity
{

  private enum ValidationStatus
  {
    NO_INPUT,
    INPUT_NOT_A_NUMBER,
    INVALID_PICK,
    INVALID_POOL,
    PICK_GREATER_THAN_POOL,
    SUCCESS;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void generateButtonClick(View view)
  {
    /* Sanity check: Check if the button clicked is a button. */
    if (view instanceof Button)
    {
      Button button = (Button) view;

       /* Sanity check: Check if the button clicked was actually the generate button. */
      if (button == findViewById(R.id.generateButton))
      {
        /* Validate the input. */
        TextView pickedTextView = findTextViewById(R.id.pickedNumbersTextField);
        TextView poolTextView = findTextViewById(R.id.poolNumberTextField);

        String pickString = pickedTextView.getText().toString();
        String poolString = poolTextView.getText().toString();

        ValidationStatus status = validateInput(pickString, poolString);

        if (status == ValidationStatus.SUCCESS)
        {
            Integer pick = Integer.parseInt(pickString);
            Integer pool = Integer.parseInt(poolString);

            /* Generate the numbers */
            SimpleLotteryPicksGenerator generator
                = SimpleLotteryPicksGenerator.createSimpleLotteryPicksGenerator();

            ArrayList<Integer> picks = generator.generateNumberPicks();

            final Context context = this;

            Bundle bundle = new Bundle();
            bundle.putInt("pick", pick);
            bundle.putInt("pool", pool);
            bundle.putIntegerArrayList("results", picks);

            Intent intent = new Intent(context, ResultsActivity.class);
            intent.putExtra("main_data", bundle);
            startActivity(intent);
        }
        else
        {
          String title = "Error";
          String message =   "The app has encountered an error"
                           + "trying to validate the input.";
          String okButtonTitle = "Ok";
          switch (status)
          {
            case INVALID_PICK:
            {
              message = "The count of picks must be greater than 0.";
              break;
            }
            case INVALID_POOL:
            {
              message = "The count of pool numbers must be greater than 0.";
              break;
            }
            case PICK_GREATER_THAN_POOL:
            {
              message = "The count of picks must not be greater than the "
                  + "count of numbers in the pool.";
              break;
            }
            case NO_INPUT:
            {
              message = "Please provide values for the two input fields.";
              break;
            }
            case INPUT_NOT_A_NUMBER:
            {
              message = "Input is not a number. Only number is allowed.";
              break;
            }
          }

          new AlertDialog.Builder(this)
              .setTitle(title)
              .setMessage(message)
              .setCancelable(false)
              .setPositiveButton(okButtonTitle, null)
              .show();
        }
      }
    }
  }

    /* Convenience methods */

  /**
   * Returns a {@link android.widget.Button} based on the given id.
   *
   * @param id a string used to look up the {@link android.widget.Button}.
   * @return {@link android.widget.Button}
   */
  private Button findButtonById(int id)
  {
    return (Button) findViewById(id);
  }

  /**
   * Returns a {@link android.widget.TextView} based on the given id.
   *
   * @param id a strings used to look up the {@link android.widget.TextView}
   * @return {@link android.widget.TextView}
   */
  private TextView findTextViewById(int id)
  {
    return (TextView) findViewById(id);
  }

  /**
   * Validates the pick and the pool.
   *
   * @param pickString The count of numbers to be picked from the pool in String type.
   * @param poolString The count of numbers in the pool in String type.
   * @return {@link com.raymond.drawgenerator.MainActivity.ValidationStatus}
   */
  private ValidationStatus validateInput(String pickString, String poolString)
  {
    /* Defaults to success. */
    ValidationStatus code = ValidationStatus.SUCCESS;

    if (pickString.length() > 0 && poolString.length() > 0)
    {
      try
      {
        Integer pick = Integer.parseInt(pickString);
        Integer pool = Integer.parseInt(poolString);

        if (pick <= 0)
        {
          /* Invalid case. There must be at least one pick */
          code = ValidationStatus.INVALID_PICK;
        } else if (pool <= 0)
        {
          /* Invalid case. Pool must be greater than 0. */
          code = ValidationStatus.INVALID_POOL;
        } else if (pick > pool)
        {
          /* Invalid case. Pool must be greater than or equal to pick. */
          code = ValidationStatus.PICK_GREATER_THAN_POOL;
        }
      } catch (NumberFormatException e)
      {
        /* Invalid case. Input is not a number. */
        code = ValidationStatus.INPUT_NOT_A_NUMBER;
      }
    } else
    {
      /* Invalid case: Input is not complete. Either the pick is missing or the pool. */
      code = ValidationStatus.NO_INPUT;
    }
    return code;
  }

}
