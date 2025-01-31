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
package com.dremio.exec.store.metadatarefresh.dirlisting;

import com.dremio.common.exceptions.ExecutionSetupException;
import com.dremio.sabot.exec.context.OperatorContext;
import com.dremio.sabot.exec.fragment.FragmentExecutionContext;
import com.dremio.sabot.op.scan.ScanOperator;
import com.dremio.sabot.op.spi.ProducerOperator;

public class DirListingOperatorCreator implements ProducerOperator.Creator<DirListingSubScan> {
  @Override
  public ProducerOperator create(FragmentExecutionContext fragmentExecContext, OperatorContext context, DirListingSubScan config) throws ExecutionSetupException {
      // TODO : Create an record reader for each partition directory
    return new ScanOperator(config, context, new DirListingRecordReaderIterator(context, fragmentExecContext, config));
  }
}
