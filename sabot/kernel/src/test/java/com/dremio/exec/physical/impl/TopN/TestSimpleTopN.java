/*
 * Copyright (C) 2017-2019 Dremio Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dremio.exec.physical.impl.TopN;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.arrow.vector.BigIntVector;
import org.junit.Ignore;
import org.junit.Test;

import com.dremio.common.expression.SchemaPath;
import com.dremio.exec.client.DremioClient;
import com.dremio.exec.pop.PopUnitTestBase;
import com.dremio.exec.record.RecordBatchLoader;
import com.dremio.exec.server.SabotNode;
import com.dremio.sabot.rpc.user.QueryDataBatch;
import com.dremio.service.coordinator.ClusterCoordinator;
import com.dremio.service.coordinator.local.LocalClusterCoordinator;

public class TestSimpleTopN extends PopUnitTestBase {
  static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TestSimpleTopN.class);

  @Ignore("DX-3872")
  @Test
  public void sortOneKeyAscending() throws Throwable{
        try (ClusterCoordinator clusterCoordinator = LocalClusterCoordinator.newRunningCoordinator();
             SabotNode bit1 = new SabotNode(DEFAULT_SABOT_CONFIG, clusterCoordinator, CLASSPATH_SCAN_RESULT, true);
             SabotNode bit2 = new SabotNode(DEFAULT_SABOT_CONFIG, clusterCoordinator, CLASSPATH_SCAN_RESULT, false);
             DremioClient client = new DremioClient(DEFAULT_SABOT_CONFIG, clusterCoordinator)) {

      bit1.run();
      bit2.run();
      client.connect();
      List<QueryDataBatch> results = client.runQuery(com.dremio.exec.proto.UserBitShared.QueryType.PHYSICAL,
        readResourceAsString("/topN/one_key_sort.json"));
      int count = 0;
      for (QueryDataBatch b : results) {
        if (b.getHeader().getRowCount() != 0) {
          count += b.getHeader().getRowCount();
        }
      }
      assertEquals(100, count);

      long previousBigInt = Long.MIN_VALUE;

      int recordCount = 0;
      int batchCount = 0;

      for (QueryDataBatch b : results) {
        if (b.getHeader().getRowCount() == 0) {
          continue;
        }
        batchCount++;
        RecordBatchLoader loader = new RecordBatchLoader(bit1.getContext().getAllocator());
        loader.load(b.getHeader().getDef(),b.getData());
        BigIntVector c1 = loader.getValueAccessorById(BigIntVector.class, loader.getValueVectorId(new SchemaPath("blue")).getFieldIds()).getValueVector();

        for (int i =0; i < c1.getValueCount(); i++) {
          recordCount++;
          assertTrue(previousBigInt <= c1.get(i));
          previousBigInt = c1.get(i);
        }
        loader.clear();
        b.release();
      }
      System.out.println(String.format("Sorted %,d records in %d batches.", recordCount, batchCount));

    }

  }

}
