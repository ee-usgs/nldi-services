package gov.usgs.owi.nldi.controllers;

import java.math.BigInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Pattern;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.usgs.owi.nldi.dao.LookupDao;
import gov.usgs.owi.nldi.dao.StreamingDao;
import gov.usgs.owi.nldi.services.ConfigurationService;
import gov.usgs.owi.nldi.services.LogService;
import gov.usgs.owi.nldi.services.Navigation;
import gov.usgs.owi.nldi.services.Parameters;

@RestController
@RequestMapping(value="linked-data/comid/{comid}/navigate/{navigationMode}")
public class NetworkController extends BaseController {

	@Autowired
	public NetworkController(LookupDao inLookupDao, StreamingDao inStreamingDao,
			Navigation inNavigation, Parameters inParameters, ConfigurationService configurationService,
			LogService inLogService) {
		super(inLookupDao, inStreamingDao, inNavigation, inParameters, configurationService, inLogService);
	}

	//swagger documentation for /linked-data/{featureSource}/{featureID}/navigate/{navigationMode} endpoint
	@Operation(summary = "getFlowlines", description = "returns the flowlines for the specified navigation in WGS84 lat/lon GeoJSON")
	@GetMapping(produces=MediaType.APPLICATION_JSON_VALUE)
	@Deprecated
	public void getFlowlines(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(Parameters.COMID) @Range(min=1, max=Integer.MAX_VALUE) String comid,
			@PathVariable(Parameters.NAVIGATION_MODE) @Pattern(regexp=REGEX_NAVIGATION_MODE) String navigationMode,
			@RequestParam(value=Parameters.STOP_COMID, required=false) @Range(min=1, max=Integer.MAX_VALUE) String stopComid,
			@Parameter(description=Parameters.DISTANCE_DESCRIPTION)
				@RequestParam(value=Parameters.DISTANCE, required=false, defaultValue=Parameters.MAX_DISTANCE)
			@Pattern(message=Parameters.DISTANCE_VALIDATION_MESSAGE, regexp=Parameters.DISTANCE_VALIDATION_REGEX) String distance,
			@RequestParam(value=Parameters.LEGACY, required=false) String legacy) throws Exception {
		BigInteger logId = logService.logRequest(request);
		try {
			streamFlowLines(response, comid, navigationMode, stopComid, distance, isLegacy(legacy, navigationMode));
		} catch (Exception e) {
			GlobalDefaultExceptionHandler.handleError(e, response);
		} finally {
			logService.logRequestComplete(logId, response.getStatus());
		}
	}

	//swagger documentation for /linked-data/{featureSource}/{featureID}/navigate/{navigationMode}/{dataSource} endpoint
	@Operation(summary = "getFeatures", description = "Returns all features found along the specified navigation as points in WGS84 lat/lon GeoJSON")
	@GetMapping(value="{dataSource}", produces=MediaType.APPLICATION_JSON_VALUE)
	public void getFeatures(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(Parameters.COMID) @Range(min=1, max=Integer.MAX_VALUE) String comid,
			@PathVariable(Parameters.NAVIGATION_MODE) @Pattern(regexp=REGEX_NAVIGATION_MODE) String navigationMode,
			@PathVariable(value=DATA_SOURCE) String dataSource,
			@RequestParam(value=Parameters.STOP_COMID, required=false) @Range(min=1, max=Integer.MAX_VALUE) String stopComid,
			@Parameter(description=Parameters.DISTANCE_DESCRIPTION)
				@RequestParam(value=Parameters.DISTANCE, required=false, defaultValue=Parameters.MAX_DISTANCE)
			@Pattern(message=Parameters.DISTANCE_VALIDATION_MESSAGE, regexp=Parameters.DISTANCE_VALIDATION_REGEX) String distance,
			@RequestParam(value=Parameters.LEGACY, required=false) String legacy) throws Exception {
		BigInteger logId = logService.logRequest(request);
		try {
			streamFeatures(response, comid, navigationMode, stopComid, distance, dataSource, isLegacy(legacy, navigationMode));
		} catch (Exception e) {
			GlobalDefaultExceptionHandler.handleError(e, response);
		} finally {
			logService.logRequestComplete(logId, response.getStatus());
		}
	}

}
