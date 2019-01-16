package io.mosip.authentication.service.impl.indauth.service.bio;

import java.util.Map;
import java.util.function.BiFunction;

import io.mosip.authentication.core.constant.IdAuthenticationErrorConstants;
import io.mosip.authentication.core.exception.IdAuthenticationBusinessException;
import io.mosip.authentication.core.spi.indauth.match.MatchFunction;
import io.mosip.authentication.core.spi.indauth.match.MatchingStrategy;
import io.mosip.authentication.core.spi.indauth.match.MatchingStrategyType;
import io.mosip.authentication.core.spi.irisauth.provider.IrisProvider;

public enum IrisMatchingStrategy implements MatchingStrategy {
	/** The Constant idvid. */
	
	
	
	PARTIAL(MatchingStrategyType.PARTIAL,(Object reqInfo,Object entityInfo,Map<String,Object> props)->{
		 
		
		if (reqInfo instanceof Map && entityInfo instanceof Map) {
			Object object = props.get(IrisProvider.class.getSimpleName());
			if (object instanceof BiFunction) {
				BiFunction<Map<String, String>, Map<String, String>, Double> func = (BiFunction<Map<String, String>, Map<String, String>, Double>) object;
				Map<String, String> reqInfoMap=(Map<String, String>) reqInfo;
				reqInfoMap.put(getIdvid(), (String)props.get(getIdvid()));  //FIXME will be removed when iris sdk is provided
				return (int) func.apply(reqInfoMap, (Map<String, String>) entityInfo).doubleValue();
			}else {
				throw new IdAuthenticationBusinessException(IdAuthenticationErrorConstants.UNKNOWN_ERROR);
			}
		}
		return 0;
	});
	
	
	
	
	
	
	
	
	
	
	
	private static final String IDVID = "idvid";


	private IrisMatchingStrategy(MatchingStrategyType matchStrategyType,MatchFunction matchFunction) {
		this.matchStrategyType=matchStrategyType;
		this.matchFunction=matchFunction;
		
	}
	

	
	private MatchingStrategyType matchStrategyType;
	
	private MatchFunction matchFunction;
	

	@Override
	public MatchingStrategyType getType() {
		return matchStrategyType;
	}

	@Override
	public MatchFunction getMatchFunction() {
		return matchFunction;
	}

	@Override
	public int match(Map<String, String> reqValues, Map<String, String> entityValues,
			Map<String, Object> matchProperties) throws IdAuthenticationBusinessException {
		
		return matchFunction.match(reqValues, entityValues, matchProperties);
	}

	public static String getIdvid() {
		return IDVID;
	}
	
	

}
