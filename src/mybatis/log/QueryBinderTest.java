package mybatis.log;

import java.io.IOException;

class QueryBinderTest {

	public static void main(String[] args) throws IOException {
		String log =  "2020-06-29 18:00:02.985 DEBUG [dScheduler-1011] c.t.c.infrastructure.persistence.mybatis.meter.MeterMapper.findList     143 : ==>  Preparing: -- com.toast.cloud.infrastructure.persistence.mybatis.meter.MeterMapper.findList SELECT seq, app_key, org_id, project_id, product_id, resource_id, parent_resource_id, resource_name, resource_group_seq, counter_name, counter_type, counter_volume, counter_value, counter_unit, timestamp, hour, source, insert_time, gmid FROM product_meter_2020_06 WHERE timestamp >= ? AND timestamp < ? AND ( org_id IN ( ? , ? , ? , ? , ? , ? ) OR project_id IN ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ) ) AND app_key IN ( ? ) AND counter_name IN ( ? ) LIMIT ?, ?\n"
					 + "2020-06-29 18:00:02.985 DEBUG [dScheduler-1011] c.t.c.infrastructure.persistence.mybatis.meter.MeterMapper.findList     143 : ==> Parameters: 2020-06-28 00:00:00.0(Timestamp), 2020-06-29 00:00:00.0(Timestamp), 1XslZcTF1ou6UJRC(String), GKForcuCaGhONWOq(String), PGNqg3CcFBFc04HK(String), Thm2rkigOL8aT0YM(String), XaDMOV1WKQjbxZgW(String), qq3Q1fwWxKSOQ3MD(String), 2sgCABAs(String), 6R9W1nC3(String), 7MZelDy6(String), 8IU7lWHM(String), AbluIyiZ(String), SOAl4uT8(String), bQ53AMAM(String), c80UHSym(String), esvfsfve(String), fYpvCj4H(String), fm0yfxlM(String), mFROrIIE(String), oFoqcRmH(String), VRRu3B6L(String), yVLRfkLy(String), d8BEBiLF(String), Ktfpjs87(String), X1nR8O4v(String), jO4ZlayB(String), f2ko9cxF(String), 4fjxjyXh(String), 1n3oYQhbzp4peLbI(String), iaas.kr1.network.traffic(String), 0(Integer), 72(Integer)\n";

		QueryBinder queryBinder = new QueryBinder(log);
		String actual = queryBinder.bind();

		System.out.println(actual);
	}


}