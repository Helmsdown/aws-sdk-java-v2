/*
 * Copyright 2010-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package software.amazon.awssdk.extensions.dynamodb.mappingclient;

import software.amazon.awssdk.annotations.SdkPublicApi;
import software.amazon.awssdk.extensions.dynamodb.mappingclient.core.DynamoDbMappedTable;

/**
 * Interface for running commands against an object that is linked to a specific DynamoDb secondary index and knows
 * how to map records from the table that index is linked to into a modelled object. This interface is extended by the
 * {@link DynamoDbMappedTable} interface as all commands that can be made against a secondary index can also be made
 * against the primary index.
 *
 * Typically an implementation for this interface can be obtained from a {@link MappedTable} which in turn can be
 * obtained from a {@link MappedDatabase}:
 *
 * mappedIndex = mappedDatabase.table(tableSchema).index("gsi_1");
 *
 * @param <T> The type of the modelled object.
 */
@SdkPublicApi
public interface MappedIndex<T> {
    /**
     * Executes a command against the database with the context of the specific secondary index this object is linked
     * to.
     *
     * Example: mappedIndex.execute(Scan.create());
     *
     * @param operationToPerform The operation to be performed in the context of the index.
     * @param <R> The expected return type from the operation. This is typically inferred by the compiler.
     * @return The result of the operation being executed. The documentation on the operation itself should have more
     * information.
     */
    <R> R execute(TableOperation<T, ?, ?, R> operationToPerform);

    /**
     * Gets the {@link MapperExtension} associated with this mapped resource.
     * @return The {@link MapperExtension} associated with this mapped resource.
     */
    MapperExtension getMapperExtension();

    /**
     * Gets the {@link OperationContext} that can be used to execute commands against.
     *
     * @return The {@link OperationContext} associated with this object.
     */
    OperationContext getOperationContext();

    /**
     * Gets the {@link TableSchema} object that this mapped table was built with.
     * @return The {@link TableSchema} object for this mapped table.
     */
    TableSchema<T> getTableSchema();

    /**
     * Creates a {@link Key} object from a modelled item. This key can be used in query conditionals and get
     * operations to locate a specific record.
     * @param item The item to extract the key fields from.
     * @return A key that has been initialized with the index values extracted from the modelled object.
     */
    Key keyFrom(T item);
}
