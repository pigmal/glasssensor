package com.pigmal.glass.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SensorFragment extends Fragment {

	private TextView mAccelerometer;
	private TextView mCompass;
	private TextView mGyro;
	private TextView mLight;
	
	private SensorManager mSensorManager;
	private AllSensorListener mListener;
	
	public SensorFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_sensor, container, false);
		mAccelerometer = (TextView)rootView.findViewById(R.id.sensor_accelerometer);
		mCompass = (TextView)rootView.findViewById(R.id.sensor_compass);
		mGyro = (TextView)rootView.findViewById(R.id.sensor_gyro);
		mLight = (TextView)rootView.findViewById(R.id.sensor_light);
		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mSensorManager = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);
		
		// 加速度、コンパス、ジャイロ、照度の各センサを取得する
		Sensor accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		Sensor compass = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		Sensor gyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		Sensor light = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		
		//リスナーを設定する
		mListener = new AllSensorListener();
		mSensorManager.registerListener(mListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(mListener, compass, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(mListener, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(mListener, light, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void onPause() {
		mSensorManager.unregisterListener(mListener);
		super.onPause();
	}
	
	
	private class AllSensorListener implements SensorEventListener {
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			float[] values = event.values;
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < values.length; i++) {
				String sValue = String.format("%+10f", values[i]);
				sb.append(sValue + ", ");
			}
			
			switch (event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				mAccelerometer.setText("Accelerometer : " + sb.toString());
				break;
			case Sensor.TYPE_MAGNETIC_FIELD:
				mCompass.setText("Compass : " + sb.toString());
				break;
			case Sensor.TYPE_GYROSCOPE:
				mGyro.setText("Gyro : " + sb.toString());
				break;
			case Sensor.TYPE_LIGHT:
				mLight.setText("Light : " + sb.toString());
				break;
			default:
				break;
			}
		}
	}
}
