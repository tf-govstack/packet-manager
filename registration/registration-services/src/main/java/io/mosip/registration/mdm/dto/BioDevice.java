package io.mosip.registration.mdm.dto;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.registration.exception.RegBaseCheckedException;
import io.mosip.registration.mdm.constants.MosipBioDeviceConstants;
import io.mosip.registration.mdm.integrator.IMosipBioDeviceIntegrator;
import io.mosip.registration.mdm.util.MdmRequestResponseBuilder;
import lombok.Getter;
import lombok.Setter;

/**
 * Holds the Biometric Device details
 * 
 * @author balamurugan.ramamoorthy
 *
 */
@Getter
@Setter
public class BioDevice {

	private String deviceType;
	private String deviceSubType;
	private String deviceModality;
	private int runningPort;
	private String runningUrl;
	private String status;
	private String providerName;
	private String providerId;
	private String serialVersion;
	private String certification;
	private String callbackId;
	private String deviceModel;
	private String deviceMake;
	private String firmWare;
	private String deviceExpiry;
	private String deviceId;
	private String deviceSubId;
	private String deviceProviderName;
	private String deviceProviderId;
	private String timestamp;
	
	private Map<String, String>  deviceSubIdMapper = new HashMap<String,String>() {
		{

			put("LEFT", "1");
			put("RIGHT", "2");
			put("THUMB", "3");
		}
	};
	

	private IMosipBioDeviceIntegrator mosipBioDeviceIntegrator;

	public CaptureResponseDto capture() throws RegBaseCheckedException {

		String url = runningUrl + ":" + runningPort + "/" + MosipBioDeviceConstants.CAPTURE_ENDPOINT;

		/* build the request object for capture */
		CaptureRequestDto mosipBioCaptureRequestDto = MdmRequestResponseBuilder.buildMosipBioCaptureRequestDto(this);

		return mosipBioDeviceIntegrator.capture(url, mosipBioCaptureRequestDto, CaptureResponseDto.class);

	}
	
	public InputStream stream() throws IOException {

		String url = runningUrl + ":" + runningPort + "/" + MosipBioDeviceConstants.STREAM_ENDPOINT;

		/* build the request object for capture */
		CaptureRequestDto mosipBioCaptureRequestDto = MdmRequestResponseBuilder.buildMosipBioCaptureRequestDto(this);

		HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
		con.setRequestMethod("POST");
		String request = new ObjectMapper().writeValueAsString(mosipBioCaptureRequestDto);

		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(request);
		wr.flush();
		wr.close();
		con.setReadTimeout(5000);
		con.connect();
		InputStream urlStream = con.getInputStream();
		
		return urlStream;


	}

	public byte[] forceCapture() {
		return null;

	}
	
	public void buildDeviceSubId(String slapType) {
		setDeviceSubId(deviceSubIdMapper.get(slapType));
	}

	public int deviceStatus() {
		return 0;

	}

}
