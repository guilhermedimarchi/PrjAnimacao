/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Principal;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.data.FloatSample;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.PitchDetector;
import com.jsyn.unitgen.VariableRateDataReader;
import com.jsyn.unitgen.VariableRateMonoReader;
import com.jsyn.unitgen.VariableRateStereoReader;
import com.jsyn.util.SampleLoader;
import com.softsynth.jsyn.view.WaveDisplay;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JSynPlayer implements Runnable{
	
        private static Synthesizer synth;
        private static VariableRateDataReader samplePlayer;
        private static LineOut lineOut;
        private ArrayList <FloatSample> samples = new ArrayList<FloatSample>();
        private double intervalo=0.0000005;
        private static boolean init;
        private static JSynPlayer player;
        private double output =0;
        public PitchDetector musica;
	 
        JSynPlayer()
        {
        }

        public double getOutput() {
            return output;
        }

        public void setOutput(double output) {
            this.output = output;
        }
         
         

	 public static JSynPlayer getInstance()
	 {
		 if(player == null)
			 player = new JSynPlayer();
		 
		 return player;
	 }
	  
	 public void init()
	 {
		 if(!init)
		 {
			 synth = JSyn.createSynthesizer();
			 synth.add( lineOut = new LineOut());
		 }
	 }
	 
	 public void addPlayList(String music)
	 {
		 if(!init)
		 {
			URL sampleFile;
			try
			{
				String url = "file:" + JSynPlayer.class.getResource(music).getPath();
				sampleFile = new URL(url);
			} 
			catch( MalformedURLException e )
			{
				e.printStackTrace();
				return;
			}
			
			
			FloatSample sample;
			try
			{
				SampleLoader.setJavaSoundPreferred( false );
				sample = SampleLoader.loadFloatSample( sampleFile );
                                samples.add(sample);
			}
			catch(IOException e)
			{
				e.printStackTrace();
				return;
			}
		 }
				
	 }
	 
	 
	 
	 public void play()
	 {
		 if(!init)
		 {
			 Thread t = new Thread(this);
			 t.start();
			 init = true;
		 }
		 		 
	 }

	
		

	@Override
	public void run() {
		
		 try
		 {
		 	for(FloatSample sample: samples)
		 	{
                            
				System.out.println( "Sample has: channels  = " + sample.getChannelsPerFrame() );
				System.out.println( "            frames    = " + sample.getNumFrames() );
				System.out.println( "            rate      = " + sample.getFrameRate() );
				
				
				
				if( sample.getChannelsPerFrame() == 1 )
				{
					synth.add( samplePlayer = new VariableRateMonoReader() );
					samplePlayer.output.connect( 0, lineOut.input, 0 );
				}
				else if( sample.getChannelsPerFrame() == 2 )
				{
					synth.add( samplePlayer = new VariableRateStereoReader() );
					samplePlayer.output.connect( 0, lineOut.input, 0 );
					samplePlayer.output.connect( 1, lineOut.input, 1 );
				}
				else
				{
					throw new RuntimeException(
							"Can only play mono or stereo samples." );
				}

				synth.start();
				lineOut.start();
				
				samplePlayer.rate.set( sample.getFrameRate() );
				
				
				PitchDetector pitch = new PitchDetector();
				samplePlayer.output.connect(0,pitch.input,0);
				synth.add(pitch);
				pitch.start();
                                
				
				
				if( sample.getSustainBegin() < 0 )
				{
					System.out.println( "queue the sample" );
					samplePlayer.dataQueue.queue( sample );
				}
				else
				{
					System.out.println( "queueOn the sample" );
					samplePlayer.dataQueue.queueOn( sample );
					synth.sleepFor( 8.0 );
					System.out.println( "queueOff the sample" );
					samplePlayer.dataQueue.queueOff( sample );
				}
			
                             try {
                                 FileOutputStream out = new FileOutputStream("feq.txt");
                                 DataOutputStream buffer = new DataOutputStream(out);
                                 
                             
				do
				{
                                    
                                    output  = samplePlayer.output.get();
                                    
			            if(pitch.confidence.get() > 0.3)
				    {
                                        buffer.writeChars(samplePlayer.dataQueue.getFrameCount() + " " +
                                                         pitch.frequency.get()+ " "  + 
                                                         
                                                         pitch.period.get() + " "    +
                                                         samplePlayer.output.get()   +
                                                         "\n");
                                        
                                        //System.out.println("1: "+ pitch.frequency.get());
                                    	//System.out.println("2: " +pitch.period.get());
				    	//System.out.println("3: " +samplePlayer.output.get());
				        
                                        
                                        musica = pitch;
                                        System.out.println(pitch.frequency.getValue());
                                        
				    			
				    }  
				    
         			    synth.sleepFor(intervalo);
				
				} while( samplePlayer.dataQueue.hasMore() );

                                buffer.close();
                                
				} catch (IOException ex) {
                                 Logger.getLogger(JSynPlayer.class.getName()).log(Level.SEVERE, null, ex);
                             }
                               
				synth.sleepFor( 0.005 );

		 	}
		 	
		 }
		 catch( InterruptedException e )
		 {
			e.printStackTrace();
	   	}

		synth.stop();
		lineOut.stop();

		
	}


	public static void main(String args[])
        {
            
        
        }

	

				
}
