/*
 *   Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package org.wso2.carbon.mdm.services.android.services;

import io.swagger.annotations.*;
import org.wso2.carbon.mdm.services.android.bean.DeviceState;
import org.wso2.carbon.mdm.services.android.bean.wrapper.EventBeanWrapper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(value = "EventReceiver", description = "Event publishing/retrieving related APIs.")
@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface EventReceiverService {

    @POST
    @Path("/publish")
    @ApiOperation(
            consumes = MediaType.APPLICATION_JSON,
            httpMethod = "POST",
            value = "Event publishing via REST API.",
            notes = "Publish events received by the EMM Android client to WSO2 DAS using this API."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created. \n Event is published successfully. Location header " +
            "contains URL of newly enrolled device",
                    responseHeaders = {
                            @ResponseHeader(name = "Location", description = "URL of the newly published event.")
                    }),
            @ApiResponse(code = 400, message = "Bad Request. \n Invalid request or validation error."),
            @ApiResponse(code = 500, message = "Internal Server Error. \n" +
                    " Error occurred while publishing the events from Android agent.")
    })
    Response publishEvents(
            @ApiParam(name = "eventBeanWrapper", value = "Information of the agent event to be published on DAS.")
            EventBeanWrapper eventBeanWrapper);

    @GET
    @ApiOperation(
            produces = MediaType.APPLICATION_JSON,
            httpMethod = "GET",
            value = "Getting event details for a given time period.",
            notes = "Get the event details of a device for a given time duration using this API.Request must contain " +
                    "the device identifier. Optionally, both, date from and date to value should be present to get " +
                    "alerts between times. Based on device type and the device identifier also filtering can be done" +
                    "(This cannot be combined with to and from parameters).",
            response = DeviceState.class,
            responseContainer = "List"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Created \n Event details of a device for a given time duration",
                    response = DeviceState.class, responseContainer = "List"),
            @ApiResponse(code = 303, message = "See Other. \n Source can be retrieved from the URL specified at" +
                    " the Location header.",
                    responseHeaders = {
                            @ResponseHeader(name = "Location", description = "The Source URL of the document.")
                    }),
            @ApiResponse(code = 304, message = "Not Modified. \n " +
                    "Empty body because the client already has the latest version of the requested resource."),
            @ApiResponse(code = 400, message = "Bad Request. \n Invalid request or validation error. You must provide" +
                    " the device identifier. Additionally, the device identifier can be combined with either the type" +
                    " OR date from and to."),
            @ApiResponse(code = 404, message = "Not Found. \n Resource requested does not exist."),
            @ApiResponse(code = 500, message = "Error occurred while getting published events for specific device.")
    })
    Response retrieveAlerts(
            @ApiParam(name = "id", value = "Device Identifier to be need to retrieve events.", required = true)
            @QueryParam("id") String deviceId,
            @ApiParam(name = "from", value = "From Date.")
            @QueryParam("from") long from,
            @ApiParam(name = "to", value = "To Date.")
            @QueryParam("to") long to,
            @ApiParam(name = "type", value = "Type of the Alert to be need to retrieve events.")
            @QueryParam("type") String type);

}
