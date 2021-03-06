/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.giraph.io.formats;

import com.google.common.collect.Lists;
import org.apache.giraph.edge.Edge;
import org.apache.giraph.edge.EdgeFactory;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
  * VertexInputFormat that features <code>long</code> vertex ID's,
  * <code>long</code> vertex values and <code>null</code>
  * out-edge weights, and <code>long</code> message types,
  *  specified in JSON format.
  */
public class JsonLongLongNullLongVertexInputFormat extends
  TextVertexInputFormat<LongWritable, LongWritable, NullWritable> {

  @Override
  public TextVertexReader createVertexReader(InputSplit split,
      TaskAttemptContext context) {
    return new JsonLongLongNullLongVertexReader();
  }

 /**
  * VertexReader that features <code>long</code> vertex
  * values and <code>null</code> out-edge weights. The
  * files should be in the following JSON format:
  * JSONArray(<vertex id>, <vertex value>,
  *   JSONArray(JSONArray(<dest vertex id>, <edge value>), ...))
  * Here is an example with vertex id 1, vertex value 4, and two edges.
  * First edge has a destination vertex 2, edge value 2.
  * Second edge has a destination vertex 3, edge value 1.
  * [1,4,[[2,2],[3,1]]]
  */
  class JsonLongLongNullLongVertexReader extends
    TextVertexReaderFromEachLineProcessedHandlingExceptions<JSONArray,
    JSONException> {

    @Override
    protected JSONArray preprocessLine(Text line) throws JSONException {
      return new JSONArray(line.toString());
    }

    @Override
    protected LongWritable getId(JSONArray jsonVertex) throws JSONException,
              IOException {
      return new LongWritable(jsonVertex.getLong(0));
    }

    @Override
    protected LongWritable getValue(JSONArray jsonVertex) throws
      JSONException, IOException {
      return new LongWritable(jsonVertex.getLong(1));
    }

    @Override
    protected Iterable<Edge<LongWritable, NullWritable>> getEdges(
        JSONArray jsonVertex) throws JSONException, IOException {
      JSONArray jsonEdgeArray = jsonVertex.getJSONArray(2);
      List<Edge<LongWritable, NullWritable>> edges =
          Lists.newArrayListWithCapacity(jsonEdgeArray.length());
      for (int i = 0; i < jsonEdgeArray.length(); ++i) {
        JSONArray jsonEdge = jsonEdgeArray.getJSONArray(i);
        edges.add(EdgeFactory.create(new LongWritable(jsonEdge.getLong(0)),
                                     NullWritable.get()));
      }
      return edges;
    }

    @Override
    protected Vertex<LongWritable, LongWritable, NullWritable,
              LongWritable> handleException(Text line, JSONArray jsonVertex,
                  JSONException e) {
      throw new IllegalArgumentException(
          "Couldn't get vertex from line " + line, e);
    }

  }
}
