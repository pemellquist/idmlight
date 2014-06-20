# openidm API Specification 

**Date:** June 17, 2014

**Document Version:** 0.1

## 1. Overview

This guide is intended for software developers who wish to create applications using the openidm set of APIs. It assumes the reader has a general understanding of APIs, IDM concepts, RESTful web services, HTTP/1.1 conventions and JSON serialization formats.

### 1.1 API Maturity Level

This API definition represents the openidm service in Beta release form. Functionality represented within this specification is subject to change. 

**Version API Status**: *BETA*


## 2. Architecture View
Role based access controls (RBAC) simplify routine account management operations and facilitate security audits. System administrators do not assign permissions directly to individual user accounts. Instead, individuals acquire access through their roles within an organization, which eliminates the need to edit a potentially large (and frequently changing) number of resource permissions and user rights assignments when creating, modifying, or deleting user accounts. Unlike traditional access control lists, permissions in RBAC describe meaningful operations within a particular application or system instead of the underlying low-level data object access methods. Storing roles and permissions in a centralized database or directory service simplifies the process of ascertaining and controlling role memberships and role permissions.

### 2.1 Overview

The openidm service is a set of APIs that provide a RESTful interface for the creation and management of identities to be used for the purpose of authorization and Role Based Access Control (RBAC). OpenIDM does not perform authenication or authorization itself since this is the function of other systems. Openidm does allow the creation and modeling of identities to be used by these systems. 

### 2.2 Conceptual/Logical Architecture View
To use the openidm API effectively, you should understand several key concepts.

#### 2.2.1 Domain 
A Domain is a container for grouping Users. A Domain can have more than one User and a User can belong to more than one Domain. A Domain can be used to group a set of Users and Roles modeling an organization. For example, a Domain can be used to represent a company with all its employees each with different access rights. 

#### 2.2.2 User 
A User is the definition of a user of a system. A can belong to one or more domains and a user may have one or more roles assigned to it.

#### 2.2.3 Role 
A Role defines a role of a User on the system. Roles can be defined to be any name and may serve any purpose. For example, a Role of 'Admin' can be used to define administrative roles.


## 4. General API Information 
This section describes operations and guidelines that are common to openidm APIs.

### 4.1 JSON 
The openidm API currently only supports JSON data serialization formats for request and response bodies. The request format is specified using the 'Content-Type' header and is required for operations that have a request body. The response format should be specified in requests using the 'Accept'header. If no response format is specified, JSON is the default.

### 4.2 Persistent Connections
By default, the API supports persistent connections via HTTP/1.1 'keep-alive's. All connections will be kept alive unless the connection header is set to close. In adherence with the IETF HTTP RFCs, the server may close the connection at any time and clients should not rely on this behavior.

### 4.3 Faults
When issuing a openidm API request, it is possible that an error can occur. In these cases, the system will return an HTTP error response code denoting the type of error and a openidm response body with additional details regarding the error. Specific HTTP status codes possible are listed in each API definition.


## 5. OpenIDM API Resources and Methods 
The following is a summary of all supported OpenIDM API resources and methods. Each resource and method is defined in detail in the subsequent sections. 

**Derived resource identifiers:**

**{baseURI}** is the endpoint URI for the openIDM REST service. This may be empty or a defined URI prefix. 

**{ver}** is the specific version URI for the REST API version. the current version is 'v1'. 


### 5.1 OpenIDM API Summary Table

|Resource            |Operation                                 |Method |Path                                                          |
|:-------------------|:-----------------------------------------|:------|:-------------------------------------------------------------|
|Versions            |Get  API version information              |GET    |{baseURI}/                                                    | 
|Domains             |Get list of all Domains                   |GET    |{baseURI}/{ver}/domains                                       | 
|Domains             |Get a specific Domain                     |GET    |{baseURI}/{ver}/domains/{domainid}                            |
|Domains             |Create a new Domain                       |POST   |{baseURI}/{ver}/domains                                       |
|Domains             |Update a Domain                           |PUT    |{baseURI}/{ver}/domains/{domainid}                            |
|Domains             |Delete a Domain                           |DELETE |{baseURI}/{ver}/domains/{domainid}                            |

### 5.2 Common Request Headers 

*HTTP standard request headers*

**Accept** - Internet media types that are acceptable in the response.  The openidm API supports the media type 'application/json'.

**Content-Length** - The length of the request body in octets (8-bit bytes).

**Content-Type** - The Internet media type of the request body. Used with POST and PUT requests. The API supports 'application/json'.


### 5.3 Common Response Headers 

*HTTP standard response headers*

**Content-Type** - Internet media type of the response body.

**Date** - The date and time that the response was sent.

*Example*

	HTTP/1.1 200 OK
	Content-Length: 1135
	Content-Type: application/json; charset=UTF-8
	Date: Tue, 30 Oct 2012 16:22:35 GMT



## 6. Get a API Versions Supported

### 6.1 Operation 
|Resource            |Operation                                 |Method |Path                                                          |
|:-------------------|:-----------------------------------------|:------|:-------------------------------------------------------------|
|Versions            |Get API version information               |GET    |{baseURI}/                                                    |

### 6.2 Description 
This method allows querying the service for all supported versions it supports. 

### 6.3 Request Data
None required.

### 6.4 Query Parameters Supported
None required.

### 6.5 Required HTTP Header Values
None required.

### 6.6 Request Body
None required.

### 6.7 Normal Response Code 
|HTTP Status Code  |Description          |
|:-----------------|:--------------------|
|200               |OK                   |

### 6.8 Response Body
The response body contains a list of all supported versions of OpenIDM.

### 6.9 Error Response Codes 
|HTTP Status Code  |Description          |
|:-----------------|:--------------------|
|400               |Bad Request          |
|401               |Unauthorized         |
|404               |Not Found            |
|405               |Not Allowed          |
|500               |OpenIDM Fault        |


### 6.10 Example

**Curl Request**

	curl -k https://<host>

**Response**

     {
        "id": "v1.0", 
        "status": "CURRENT", 
        "updated": "2014-04-18T18:30:02.25Z"
     }

