package android.geosvr.dtn;

import android.app.Activity;
import android.content.SharedPreferences;
import android.geosvr.dtn.R;
import android.geosvr.dtn.servlib.routing.prophet.ProphetNeighbor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Android Activity to support editing DTN configuration file
 */
public class DTNConfigMenu extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.dtnsettings);
	    
	    final SharedPreferences myPrefs = this.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
	       final SharedPreferences.Editor prefsEditor = myPrefs.edit();

	    final Spinner spinner = (Spinner) findViewById(R.id.spinner_Router_Type);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	            this, R.array.Router_Types, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);
	    spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
	    spinner.setSelection(adapter.getPosition(myPrefs.getString("Router_Type", "epidemic")));
	    
	    final SeekBar Q_seekBar = (SeekBar)findViewById(R.id.Q_Value_SeekBar);
	    	//Text View to display seekbar progress value.
	    	final TextView seekBarValue = (TextView)findViewById(R.id.seekBarValue);
	    	Q_seekBar.setProgress(myPrefs.getInt("Q_Value", 50));
	       
	       //Set text view to seekbar current progress.
	       seekBarValue.setText(String.valueOf((double)Q_seekBar.getProgress()/100));
	       
	        
	   Q_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){ 	   
	   @Override
	   public void onProgressChanged(SeekBar seekBar, int progress,
	     boolean fromUser) {
	    // TODO Auto-generated method stub
	    seekBarValue.setText(String.valueOf((double)progress/100));
	   }

	   @Override
	   public void onStartTrackingTouch(SeekBar seekBar) {
	    // TODO Auto-generated method stub
	   }

	   @Override
	   public void onStopTrackingTouch(SeekBar seekBar) {
	    // TODO Auto-generated method stub
	   }
	       });
	final SeekBar P_encounter_seekbar = (SeekBar)findViewById(R.id.P_Encounter_SeekBar);
	
	final TextView P_encounter_seekbar_value = (TextView)findViewById(R.id.P_Encounter_SeekBar_Value);
	P_encounter_seekbar.setProgress(myPrefs.getInt("P_Encounter", 50));
	
	 //Set text view to seekbar current progress.
	P_encounter_seekbar_value.setText(String.valueOf((double)P_encounter_seekbar.getProgress()/100));
    
	     
	P_encounter_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){ 	   
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		// TODO Auto-generated method stub
		P_encounter_seekbar_value.setText(String.valueOf((double)progress/100));
	}
	
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	 // TODO Auto-generated method stub
	}
	
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	 // TODO Auto-generated method stub
	}
	    });
	
	final SeekBar P_encounter_first_seekbar = (SeekBar)findViewById(R.id.P_Encounter_First_SeekBar);
	
	final TextView P_encounter_first_seekbar_value = (TextView)findViewById(R.id.P_Encounter_First_SeekBar_Value);
	P_encounter_first_seekbar.setProgress(myPrefs.getInt("P_Encounter_First", 25));
	
	 //Set text view to seekbar current progress.
	P_encounter_first_seekbar_value.setText(String.valueOf((double)P_encounter_first_seekbar.getProgress()/100));
    
	     
	P_encounter_first_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){ 	   
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		// TODO Auto-generated method stub
		P_encounter_first_seekbar_value.setText(String.valueOf((double)progress/100));
	}
	
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	 // TODO Auto-generated method stub
	}
	
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	 // TODO Auto-generated method stub
	}
	    });
	final SeekBar Delta_seekbar = (SeekBar)findViewById(R.id.Delta_SeekBar);
	
	final TextView Delta_seekbar_value = (TextView)findViewById(R.id.Delta_SeekBar_Value);
	Delta_seekbar.setProgress(myPrefs.getInt("Delta", 1));
	
	 //Set text view to seekbar current progress.
	Delta_seekbar_value.setText(String.valueOf((double)Delta_seekbar.getProgress()/100));
    
	     
	Delta_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){ 	   
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		// TODO Auto-generated method stub
		Delta_seekbar_value.setText(String.valueOf((double)progress/100));
	}
	
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	 // TODO Auto-generated method stub
	}
	
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	 // TODO Auto-generated method stub
	}
	    });
	final SeekBar Alpha_seekbar = (SeekBar)findViewById(R.id.Alpha_SeekBar);
	
	final TextView Alpha_seekbar_value = (TextView)findViewById(R.id.Alpha_SeekBar_Value);
	Alpha_seekbar.setProgress(myPrefs.getInt("Alpha", 50));
	
	 //Set text view to seekbar current progress.
	Alpha_seekbar_value.setText(String.valueOf((double)Alpha_seekbar.getProgress()/100));
    
	     
	Alpha_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){ 	   
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		// TODO Auto-generated method stub
		Alpha_seekbar_value.setText(String.valueOf((double)progress/100));
	}
	
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	 // TODO Auto-generated method stub
	}
	
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	 // TODO Auto-generated method stub
	}
	    });
	final SeekBar Beta_seekbar = (SeekBar)findViewById(R.id.Beta_SeekBar);
	
	final TextView Beta_seekbar_value = (TextView)findViewById(R.id.Beta_SeekBar_Value);
	Beta_seekbar.setProgress(myPrefs.getInt("Beta", 90));
	
	 //Set text view to seekbar current progress.
	Beta_seekbar_value.setText(String.valueOf((double)Beta_seekbar.getProgress()/100));
    
	     
	Beta_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){ 	   
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		// TODO Auto-generated method stub
		Beta_seekbar_value.setText(String.valueOf((double)progress/100));
	}
	
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	 // TODO Auto-generated method stub
	}
	
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	 // TODO Auto-generated method stub
	}
	    });
	final SeekBar K_seekbar = (SeekBar)findViewById(R.id.K_SeekBar);
	
	final TextView K_seekbar_value = (TextView)findViewById(R.id.K_SeekBar_Value);
	K_seekbar.setProgress(myPrefs.getInt("K", 100));
	
	 //Set text view to seekbar current progress.
	K_seekbar_value.setText(String.valueOf((double)K_seekbar.getProgress()/100));
    
	     
	K_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){ 	   
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		// TODO Auto-generated method stub
		K_seekbar_value.setText(String.valueOf((double)progress/100));
	}
	
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	 // TODO Auto-generated method stub
	}
	
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	 // TODO Auto-generated method stub
	}
	    });
	 final Spinner Queue_Type_Spinner = (Spinner) findViewById(R.id.spinner_Queue_Type);
	    ArrayAdapter<CharSequence> Queue_adapter = ArrayAdapter.createFromResource(
	            this, R.array.Queue_Types, android.R.layout.simple_spinner_item);
	    Queue_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    Queue_Type_Spinner.setAdapter(Queue_adapter);
	    //Queue_Type_Spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
	    Queue_Type_Spinner.setSelection(Queue_adapter.getPosition(myPrefs.getString("Queue_Type", "Fifo")));
	    
	    Button Save = (Button) this
		.findViewById(R.id.DTNSETTINGS_SAVE);
Save.setOnClickListener(new OnClickListener() {

	
	public void onClick(View v) {
		prefsEditor.putInt("Q_Value", Q_seekBar.getProgress());
		prefsEditor.putInt("P_Encounter", P_encounter_seekbar.getProgress());
		prefsEditor.putInt("P_Encounter_First", P_encounter_first_seekbar.getProgress());
		prefsEditor.putInt("Delta", Delta_seekbar.getProgress());
		prefsEditor.putInt("Alpha", Alpha_seekbar.getProgress());
		prefsEditor.putInt("Beta", Beta_seekbar.getProgress());
		prefsEditor.putInt("K", K_seekbar.getProgress());
		
		//Set Prophet values
		ProphetNeighbor.P_encounter = (float) P_encounter_seekbar.getProgress()/100;
		ProphetNeighbor.P_encounter_first = (float) P_encounter_first_seekbar.getProgress()/100;
		ProphetNeighbor.delta = (float) Delta_seekbar.getProgress()/100;
		ProphetNeighbor.alpha = (float) Alpha_seekbar.getProgress()/100;
		ProphetNeighbor.beta = (float) Beta_seekbar.getProgress()/100;
		//ProphetNeighbor.k = (float) in_K/100; 
		
		prefsEditor.putString("Queue_Type", Queue_Type_Spinner.getSelectedItem().toString());
		prefsEditor.putString("Router_Type",spinner.getSelectedItem().toString());
		prefsEditor.commit();
		finish();
	}
});   
	   }
	public class MyOnItemSelectedListener implements OnItemSelectedListener {
		
		SeekBar seekBar = (SeekBar)findViewById(R.id.Q_Value_SeekBar);
		TextView seekBarValue = (TextView)findViewById(R.id.seekBarValue);
		TextView Q_Value_Title = (TextView)findViewById(R.id.Title_Q_Value);
		
		SeekBar P_encounter_seekbar = (SeekBar)findViewById(R.id.P_Encounter_SeekBar);
		TextView P_encounter_seekbar_value = (TextView)findViewById(R.id.P_Encounter_SeekBar_Value);
		TextView P_encounter_title = (TextView)findViewById(R.id.P_Encounter_Title);
		
		SeekBar P_encounter_first_seekbar = (SeekBar)findViewById(R.id.P_Encounter_First_SeekBar);
		TextView P_encounter_first_seekbar_value = (TextView)findViewById(R.id.P_Encounter_First_SeekBar_Value);
		TextView P_encounter_first_title = (TextView)findViewById(R.id.P_Encounter_First_Title);
		
		SeekBar Delta_seekbar = (SeekBar)findViewById(R.id.Delta_SeekBar);
		TextView Delta_seekbar_value = (TextView)findViewById(R.id.Delta_SeekBar_Value);
		TextView Delta_title = (TextView)findViewById(R.id.Delta_Title);
		
		SeekBar Alpha_seekbar = (SeekBar)findViewById(R.id.Alpha_SeekBar);
		TextView Alpha_seekbar_value = (TextView)findViewById(R.id.Alpha_SeekBar_Value);
		TextView Alpha_title = (TextView)findViewById(R.id.Alpha_Title);
		
		SeekBar Beta_seekbar = (SeekBar)findViewById(R.id.Beta_SeekBar);
		TextView Beta_seekbar_value = (TextView)findViewById(R.id.Beta_SeekBar_Value);
		TextView Beta_title = (TextView)findViewById(R.id.Beta_Title);
		
		SeekBar K_seekbar = (SeekBar)findViewById(R.id.K_SeekBar);
		TextView K_seekbar_value = (TextView)findViewById(R.id.K_SeekBar_Value);
		TextView K_title = (TextView)findViewById(R.id.K_Title);
		
		TextView Queue_Type_Title = (TextView)findViewById(R.id.Queue_Type_Title);
		final Spinner Queue_Type_Spinner = (Spinner) findViewById(R.id.spinner_Queue_Type);
		
	    public void onItemSelected(AdapterView<?> parent,
	        View view, int pos, long id) {
	      if(parent.getItemAtPosition(pos).toString().equals("static"))
	      {
	    	  //Prophet Values
	    	  P_encounter_seekbar.setVisibility(View.GONE);
	    	  P_encounter_seekbar_value.setVisibility(View.GONE);
	    	  P_encounter_title.setVisibility(View.GONE);
	    	  
	    	  P_encounter_first_seekbar.setVisibility(View.GONE);
	    	  P_encounter_first_seekbar_value.setVisibility(View.GONE);
	    	  P_encounter_first_title.setVisibility(View.GONE);
	    	  
	    	  Delta_seekbar.setVisibility(View.GONE);
	    	  Delta_seekbar_value.setVisibility(View.GONE);
	    	  Delta_title.setVisibility(View.GONE);
	    	  
	    	  Alpha_seekbar.setVisibility(View.GONE);
	    	  Alpha_seekbar_value.setVisibility(View.GONE);
	    	  Alpha_title.setVisibility(View.GONE);
	    	  
	    	  Beta_seekbar.setVisibility(View.GONE);
	    	  Beta_seekbar_value.setVisibility(View.GONE);
	    	  Beta_title.setVisibility(View.GONE);
	    	  
	    	  K_seekbar.setVisibility(View.GONE);
	    	  K_seekbar_value.setVisibility(View.GONE);
	    	  K_title.setVisibility(View.GONE);
	    	  
	    	  Queue_Type_Title.setVisibility(View.GONE);
	    	  Queue_Type_Spinner.setVisibility(View.GONE);
	    	  
	    	  //Epidemic values
	    	  Q_Value_Title.setVisibility(View.GONE);
	    	  seekBar.setVisibility(View.GONE);
	    	  seekBarValue.setVisibility(View.GONE);
	      }
	      else if (parent.getItemAtPosition(pos).toString().equals("prophet"))
	      {
	    	  //Prophet Values
	    	  P_encounter_seekbar.setVisibility(View.VISIBLE);
	    	  P_encounter_seekbar_value.setVisibility(View.VISIBLE);
	    	  P_encounter_title.setVisibility(View.VISIBLE);
	    	  
	    	  P_encounter_first_seekbar.setVisibility(View.VISIBLE);
	    	  P_encounter_first_seekbar_value.setVisibility(View.VISIBLE);
	    	  P_encounter_first_title.setVisibility(View.VISIBLE);
	    	  
	    	  Delta_seekbar.setVisibility(View.VISIBLE);
	    	  Delta_seekbar_value.setVisibility(View.VISIBLE);
	    	  Delta_title.setVisibility(View.VISIBLE);
	    	  
	    	  Alpha_seekbar.setVisibility(View.VISIBLE);
	    	  Alpha_seekbar_value.setVisibility(View.VISIBLE);
	    	  Alpha_title.setVisibility(View.VISIBLE);
	    	  
	    	  Beta_seekbar.setVisibility(View.VISIBLE);
	    	  Beta_seekbar_value.setVisibility(View.VISIBLE);
	    	  Beta_title.setVisibility(View.VISIBLE);
	    	  
	    	  K_seekbar.setVisibility(View.VISIBLE);
	    	  K_seekbar_value.setVisibility(View.VISIBLE);
	    	  K_title.setVisibility(View.VISIBLE);
	    	  
	    	  Queue_Type_Title.setVisibility(View.VISIBLE);
	    	  Queue_Type_Spinner.setVisibility(View.VISIBLE);
	    	  
	    	  //Epidemic values
	    	  Q_Value_Title.setVisibility(View.GONE);
	    	  seekBar.setVisibility(View.GONE);
	    	  seekBarValue.setVisibility(View.GONE);
	      }
	      else if (parent.getItemAtPosition(pos).toString().equals("epidemic"))
	      {
	    	  //Prophet Values
	    	  P_encounter_seekbar.setVisibility(View.GONE);
	    	  P_encounter_seekbar_value.setVisibility(View.GONE);
	    	  P_encounter_title.setVisibility(View.GONE);
	    	  
	    	  P_encounter_first_seekbar.setVisibility(View.GONE);
	    	  P_encounter_first_seekbar_value.setVisibility(View.GONE);
	    	  P_encounter_first_title.setVisibility(View.GONE);
	    	  
	    	  Delta_seekbar.setVisibility(View.GONE);
	    	  Delta_seekbar_value.setVisibility(View.GONE);
	    	  Delta_title.setVisibility(View.GONE);
	    	  
	    	  Alpha_seekbar.setVisibility(View.GONE);
	    	  Alpha_seekbar_value.setVisibility(View.GONE);
	    	  Alpha_title.setVisibility(View.GONE);
	    	  
	    	  Beta_seekbar.setVisibility(View.GONE);
	    	  Beta_seekbar_value.setVisibility(View.GONE);
	    	  Beta_title.setVisibility(View.GONE);
	    	  
	    	  K_seekbar.setVisibility(View.GONE);
	    	  K_seekbar_value.setVisibility(View.GONE);
	    	  K_title.setVisibility(View.GONE);
	    	  
	    	  Queue_Type_Title.setVisibility(View.GONE);
	    	  Queue_Type_Spinner.setVisibility(View.GONE);
	    	  
	    	  //Epidemic values
	    	  Q_Value_Title.setVisibility(View.VISIBLE);
	    	  seekBar.setVisibility(View.VISIBLE);
	    	  seekBarValue.setVisibility(View.VISIBLE);
	      }
	    }

	    public void onNothingSelected(AdapterView parent) {
	      // Do nothing.
	    }
	}
	
}