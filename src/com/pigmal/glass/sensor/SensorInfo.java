package com.pigmal.glass.sensor;

import java.util.Hashtable;
import java.util.Locale;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorInfo {
	
	private final SensorManager mSensorManager;
	private final Sensor mAccelerometer;
	private final Sensor mCompass;
	private final Sensor mGyroscope;
	private final Sensor mLight;
	private final AllSensorListener receiver = new AllSensorListener();
	
	private Hashtable<Integer, String> mValues = new Hashtable<Integer, String>();
	
	private SensorInfoListener mListener;
	
	public SensorInfo(Context c, SensorInfoListener listener) {		
		mSensorManager = (SensorManager)c.getSystemService(Context.SENSOR_SERVICE);
		
		// 加速度、コンパス、ジャイロ、照度の各センサを取得する
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mCompass = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		
		//リスナーを設定する
		mSensorManager.registerListener(receiver, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(receiver, mCompass, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(receiver, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(receiver, mLight, SensorManager.SENSOR_DELAY_NORMAL);
		
		mListener = listener;
	}
	
	public void destory() {

		mSensorManager.unregisterListener(receiver);
		mValues.clear();
	}
	
	private class AllSensorListener implements SensorEventListener {
			
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			Sensor sensor = event.sensor;
			float[] values = event.values;
			StringBuffer value = new StringBuffer();
			for (int i = 0; i < values.length; i++) {
				String sValue = String.format(Locale.getDefault(), "%+10f", values[i]);
				value.append(sValue + ", ");
			}
			
			mValues.put(sensor.getType(), value.toString());
			
			if (mListener != null) {
				mListener.notifyUpdate();
			}
		}		
	}
	
	public String getValue(int sensorKind) {
		
		return mValues.get(sensorKind);
	}
	
	public interface SensorInfoListener {
		void notifyUpdate();
	}
}
