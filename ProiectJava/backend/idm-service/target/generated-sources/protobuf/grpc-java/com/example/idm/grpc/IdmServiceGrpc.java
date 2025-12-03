package com.example.idm.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.64.0)",
    comments = "Source: idm.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class IdmServiceGrpc {

  private IdmServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "idm.IdmService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.example.idm.grpc.LoginRequest,
      com.example.idm.grpc.LoginResponse> getLoginMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Login",
      requestType = com.example.idm.grpc.LoginRequest.class,
      responseType = com.example.idm.grpc.LoginResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.idm.grpc.LoginRequest,
      com.example.idm.grpc.LoginResponse> getLoginMethod() {
    io.grpc.MethodDescriptor<com.example.idm.grpc.LoginRequest, com.example.idm.grpc.LoginResponse> getLoginMethod;
    if ((getLoginMethod = IdmServiceGrpc.getLoginMethod) == null) {
      synchronized (IdmServiceGrpc.class) {
        if ((getLoginMethod = IdmServiceGrpc.getLoginMethod) == null) {
          IdmServiceGrpc.getLoginMethod = getLoginMethod =
              io.grpc.MethodDescriptor.<com.example.idm.grpc.LoginRequest, com.example.idm.grpc.LoginResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Login"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.idm.grpc.LoginRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.idm.grpc.LoginResponse.getDefaultInstance()))
              .setSchemaDescriptor(new IdmServiceMethodDescriptorSupplier("Login"))
              .build();
        }
      }
    }
    return getLoginMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.idm.grpc.TokenRequest,
      com.example.idm.grpc.ValidateTokenResponse> getValidateTokenMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ValidateToken",
      requestType = com.example.idm.grpc.TokenRequest.class,
      responseType = com.example.idm.grpc.ValidateTokenResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.idm.grpc.TokenRequest,
      com.example.idm.grpc.ValidateTokenResponse> getValidateTokenMethod() {
    io.grpc.MethodDescriptor<com.example.idm.grpc.TokenRequest, com.example.idm.grpc.ValidateTokenResponse> getValidateTokenMethod;
    if ((getValidateTokenMethod = IdmServiceGrpc.getValidateTokenMethod) == null) {
      synchronized (IdmServiceGrpc.class) {
        if ((getValidateTokenMethod = IdmServiceGrpc.getValidateTokenMethod) == null) {
          IdmServiceGrpc.getValidateTokenMethod = getValidateTokenMethod =
              io.grpc.MethodDescriptor.<com.example.idm.grpc.TokenRequest, com.example.idm.grpc.ValidateTokenResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ValidateToken"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.idm.grpc.TokenRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.idm.grpc.ValidateTokenResponse.getDefaultInstance()))
              .setSchemaDescriptor(new IdmServiceMethodDescriptorSupplier("ValidateToken"))
              .build();
        }
      }
    }
    return getValidateTokenMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.idm.grpc.TokenRequest,
      com.example.idm.grpc.LogoutResponse> getLogoutMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Logout",
      requestType = com.example.idm.grpc.TokenRequest.class,
      responseType = com.example.idm.grpc.LogoutResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.idm.grpc.TokenRequest,
      com.example.idm.grpc.LogoutResponse> getLogoutMethod() {
    io.grpc.MethodDescriptor<com.example.idm.grpc.TokenRequest, com.example.idm.grpc.LogoutResponse> getLogoutMethod;
    if ((getLogoutMethod = IdmServiceGrpc.getLogoutMethod) == null) {
      synchronized (IdmServiceGrpc.class) {
        if ((getLogoutMethod = IdmServiceGrpc.getLogoutMethod) == null) {
          IdmServiceGrpc.getLogoutMethod = getLogoutMethod =
              io.grpc.MethodDescriptor.<com.example.idm.grpc.TokenRequest, com.example.idm.grpc.LogoutResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Logout"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.idm.grpc.TokenRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.idm.grpc.LogoutResponse.getDefaultInstance()))
              .setSchemaDescriptor(new IdmServiceMethodDescriptorSupplier("Logout"))
              .build();
        }
      }
    }
    return getLogoutMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static IdmServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<IdmServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<IdmServiceStub>() {
        @java.lang.Override
        public IdmServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new IdmServiceStub(channel, callOptions);
        }
      };
    return IdmServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static IdmServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<IdmServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<IdmServiceBlockingStub>() {
        @java.lang.Override
        public IdmServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new IdmServiceBlockingStub(channel, callOptions);
        }
      };
    return IdmServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static IdmServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<IdmServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<IdmServiceFutureStub>() {
        @java.lang.Override
        public IdmServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new IdmServiceFutureStub(channel, callOptions);
        }
      };
    return IdmServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void login(com.example.idm.grpc.LoginRequest request,
        io.grpc.stub.StreamObserver<com.example.idm.grpc.LoginResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getLoginMethod(), responseObserver);
    }

    /**
     */
    default void validateToken(com.example.idm.grpc.TokenRequest request,
        io.grpc.stub.StreamObserver<com.example.idm.grpc.ValidateTokenResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getValidateTokenMethod(), responseObserver);
    }

    /**
     */
    default void logout(com.example.idm.grpc.TokenRequest request,
        io.grpc.stub.StreamObserver<com.example.idm.grpc.LogoutResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getLogoutMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service IdmService.
   */
  public static abstract class IdmServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return IdmServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service IdmService.
   */
  public static final class IdmServiceStub
      extends io.grpc.stub.AbstractAsyncStub<IdmServiceStub> {
    private IdmServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected IdmServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new IdmServiceStub(channel, callOptions);
    }

    /**
     */
    public void login(com.example.idm.grpc.LoginRequest request,
        io.grpc.stub.StreamObserver<com.example.idm.grpc.LoginResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getLoginMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void validateToken(com.example.idm.grpc.TokenRequest request,
        io.grpc.stub.StreamObserver<com.example.idm.grpc.ValidateTokenResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getValidateTokenMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void logout(com.example.idm.grpc.TokenRequest request,
        io.grpc.stub.StreamObserver<com.example.idm.grpc.LogoutResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getLogoutMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service IdmService.
   */
  public static final class IdmServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<IdmServiceBlockingStub> {
    private IdmServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected IdmServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new IdmServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.example.idm.grpc.LoginResponse login(com.example.idm.grpc.LoginRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getLoginMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.idm.grpc.ValidateTokenResponse validateToken(com.example.idm.grpc.TokenRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getValidateTokenMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.idm.grpc.LogoutResponse logout(com.example.idm.grpc.TokenRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getLogoutMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service IdmService.
   */
  public static final class IdmServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<IdmServiceFutureStub> {
    private IdmServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected IdmServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new IdmServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.idm.grpc.LoginResponse> login(
        com.example.idm.grpc.LoginRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getLoginMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.idm.grpc.ValidateTokenResponse> validateToken(
        com.example.idm.grpc.TokenRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getValidateTokenMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.idm.grpc.LogoutResponse> logout(
        com.example.idm.grpc.TokenRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getLogoutMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_LOGIN = 0;
  private static final int METHODID_VALIDATE_TOKEN = 1;
  private static final int METHODID_LOGOUT = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_LOGIN:
          serviceImpl.login((com.example.idm.grpc.LoginRequest) request,
              (io.grpc.stub.StreamObserver<com.example.idm.grpc.LoginResponse>) responseObserver);
          break;
        case METHODID_VALIDATE_TOKEN:
          serviceImpl.validateToken((com.example.idm.grpc.TokenRequest) request,
              (io.grpc.stub.StreamObserver<com.example.idm.grpc.ValidateTokenResponse>) responseObserver);
          break;
        case METHODID_LOGOUT:
          serviceImpl.logout((com.example.idm.grpc.TokenRequest) request,
              (io.grpc.stub.StreamObserver<com.example.idm.grpc.LogoutResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getLoginMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.idm.grpc.LoginRequest,
              com.example.idm.grpc.LoginResponse>(
                service, METHODID_LOGIN)))
        .addMethod(
          getValidateTokenMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.idm.grpc.TokenRequest,
              com.example.idm.grpc.ValidateTokenResponse>(
                service, METHODID_VALIDATE_TOKEN)))
        .addMethod(
          getLogoutMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.idm.grpc.TokenRequest,
              com.example.idm.grpc.LogoutResponse>(
                service, METHODID_LOGOUT)))
        .build();
  }

  private static abstract class IdmServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    IdmServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.example.idm.grpc.IdmProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("IdmService");
    }
  }

  private static final class IdmServiceFileDescriptorSupplier
      extends IdmServiceBaseDescriptorSupplier {
    IdmServiceFileDescriptorSupplier() {}
  }

  private static final class IdmServiceMethodDescriptorSupplier
      extends IdmServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    IdmServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (IdmServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new IdmServiceFileDescriptorSupplier())
              .addMethod(getLoginMethod())
              .addMethod(getValidateTokenMethod())
              .addMethod(getLogoutMethod())
              .build();
        }
      }
    }
    return result;
  }
}
