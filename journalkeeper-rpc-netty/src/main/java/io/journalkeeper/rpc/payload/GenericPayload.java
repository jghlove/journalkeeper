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
package io.journalkeeper.rpc.payload;

import io.journalkeeper.rpc.remoting.transport.command.Payload;

/**
 * @author LiYue
 * Date: 2019-03-29
 */
public class GenericPayload<T> implements Payload {
    private T payload;

    public GenericPayload(T payload) {
        this.payload = payload;
    }

    @SuppressWarnings("unchecked")
    public static <P> P get(Object payload) {
        return ((GenericPayload<P>) payload).getPayload();
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
