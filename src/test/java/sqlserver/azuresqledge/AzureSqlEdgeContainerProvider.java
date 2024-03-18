/*
 * Copyright (c) 2008-2024, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sqlserver.azuresqledge;

import org.testcontainers.containers.JdbcDatabaseContainerProvider;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * See <a href="https://github.com/testcontainers/testcontainers-java/issues/5191">...</a>
 * <p>
 * Uses a {@link MSSQLServerContainer} with a `mcr.microsoft.com/azure-sql-edge` image (default: "latest") in place of
 * the standard ` mcr.microsoft.com/mssql/server` image
 */
class AzureSqlEdgeContainerProvider extends JdbcDatabaseContainerProvider {

    private static final String NAME = "sqlserver/azuresqledge";

    private static final String TEST_AZURE_SQL_EDGE_VERSION = System.getProperty("test.azuresqledge.version", "1.0.7");

    @Override
    public boolean supports(String databaseType) {
        return databaseType.equals(NAME);
    }

    @Override
    public MSSQLServerContainer<?> newInstance() {
        return newInstance(TEST_AZURE_SQL_EDGE_VERSION);
    }

    @Override
    public MSSQLServerContainer<?> newInstance(String tag) {
        var taggedImageName = DockerImageName.parse("mcr.microsoft.com/azure-sql-edge")
                .withTag(tag)
                .asCompatibleSubstituteFor("mcr.microsoft.com/mssql/server");
        return new MSSQLServerContainer<>(taggedImageName);
    }
}
