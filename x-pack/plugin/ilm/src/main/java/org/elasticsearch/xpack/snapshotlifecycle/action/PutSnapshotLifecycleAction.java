/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */

package org.elasticsearch.xpack.snapshotlifecycle.action;

import org.elasticsearch.action.Action;
import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.action.support.master.AcknowledgedRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.common.xcontent.ToXContentObject;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.xpack.snapshotlifecycle.SnapshotLifecyclePolicy;

import java.io.IOException;
import java.util.Objects;

public class PutSnapshotLifecycleAction extends Action<PutSnapshotLifecycleAction.Response> {
    public static final PutSnapshotLifecycleAction INSTANCE = new PutSnapshotLifecycleAction();
    public static final String NAME = "cluster:admin/ilm/snapshot/put";

    protected PutSnapshotLifecycleAction() {
        super(NAME);
    }

    @Override
    public PutSnapshotLifecycleAction.Response newResponse() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Writeable.Reader<PutSnapshotLifecycleAction.Response> getResponseReader() {
        return Response::new;
    }

    public static class Request extends AcknowledgedRequest<Request> implements ToXContentObject {

        private String lifecycleId;
        private SnapshotLifecyclePolicy lifecycle;

        Request(String lifecycleId, SnapshotLifecyclePolicy lifecycle) {
            this.lifecycleId = lifecycleId;
            this.lifecycle = lifecycle;
        }

        Request() { }

        public String getLifecycleId() {
            return this.lifecycleId;
        }

        public SnapshotLifecyclePolicy getLifecycle() {
            return this.lifecycle;
        }

        public static Request parseRequest(String lifecycleId, XContentParser parser) {
            return new Request(lifecycleId, SnapshotLifecyclePolicy.parse(parser, lifecycleId));
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            super.readFrom(in);
            lifecycleId = in.readString();
            lifecycle = new SnapshotLifecyclePolicy(in);
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            super.writeTo(out);
            out.writeString(lifecycleId);
            lifecycle.writeTo(out);
        }

        @Override
        public ActionRequestValidationException validate() {
            return null;
        }

        @Override
        public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
            builder.startObject();
            builder.field(lifecycleId, lifecycle);
            builder.endObject();
            return builder;
        }

        @Override
        public int hashCode() {
            return Objects.hash(lifecycleId, lifecycle);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj.getClass() != getClass()) {
                return false;
            }
            Request other = (Request) obj;
            return lifecycleId.equals(other.lifecycleId) &&
                lifecycle.equals(other.lifecycle);
        }

        @Override
        public String toString() {
            return Strings.toString(this);
        }
    }

    public static class Response extends AcknowledgedResponse implements ToXContentObject {

        public Response() { }

        public Response(boolean acknowledged) {
            super(acknowledged);
        }

        public Response(StreamInput streamInput) throws IOException {
            this(streamInput.readBoolean());
        }
    }
}
