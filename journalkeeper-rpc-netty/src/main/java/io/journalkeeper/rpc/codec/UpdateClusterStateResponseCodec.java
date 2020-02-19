/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.journalkeeper.rpc.codec;

import io.journalkeeper.rpc.client.UpdateClusterStateResponse;
import io.journalkeeper.rpc.header.JournalKeeperHeader;
import io.journalkeeper.rpc.remoting.serialize.CodecSupport;
import io.journalkeeper.rpc.remoting.transport.command.Type;
import io.netty.buffer.ByteBuf;

/**
 * @author LiYue
 * Date: 2019-03-29
 */
public class UpdateClusterStateResponseCodec extends LeaderResponseCodec<UpdateClusterStateResponse> implements Type {
    @Override
    protected void encodeLeaderResponse(UpdateClusterStateResponse leaderResponse, ByteBuf buffer) {
        CodecSupport.encodeList(buffer, leaderResponse.getResults(), (obj, buffer1) -> CodecSupport.encodeBytes(buffer, (byte[]) obj));

    }

    @Override
    protected UpdateClusterStateResponse decodeLeaderResponse(JournalKeeperHeader header, ByteBuf buffer) {
        return new UpdateClusterStateResponse(CodecSupport.decodeList(buffer, CodecSupport::decodeBytes));
    }

    @Override
    public int type() {
        return RpcTypes.UPDATE_CLUSTER_STATE_RESPONSE;
    }
}
