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

package com.dremio.exec.store.hive.deltalake;

import java.io.IOException;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.Reporter;

import com.dremio.exec.ExecConstants;
import com.dremio.options.OptionManager;

public class DeltaHiveInputFormat extends FileInputFormat<NullWritable, ArrayWritable> {
  static final String DELTA_STORAGE_HANDLER = "io.delta.hive.DeltaStorageHandler";

  public static boolean isDeltaTable(String storageHandler, OptionManager options) {
    return options.getOption(ExecConstants.ENABLE_DELTALAKE_HIVE_SUPPORT) && DELTA_STORAGE_HANDLER.equalsIgnoreCase(storageHandler);
  }

  public DeltaHiveInputFormat() {
    super();
  }

  @Override
  public RecordReader<NullWritable, ArrayWritable> getRecordReader(InputSplit inputSplit, JobConf jobConf, Reporter reporter) throws IOException {
    return null;
  }
}
