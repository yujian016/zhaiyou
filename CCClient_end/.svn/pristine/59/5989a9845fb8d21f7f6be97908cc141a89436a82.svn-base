package com.ccc.www.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ccc.ccclient_end.R;

public class UserAgreementActivity extends BaseActivity {

	ImageButton ib_register_close;
	ListView act_useragreementdetail_listview;

	List<Content> allContent = new ArrayList<Content>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.act_useragreementdetail);
		initview();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_register_close:
			UserAgreementActivity.this.finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void findviewWithId() {
		ib_register_close = (ImageButton) findViewById(R.id.ib_register_close);
		act_useragreementdetail_listview = (ListView) findViewById(R.id.act_useragreementdetail_listview);
	}

	@Override
	public void initListener() {
		ib_register_close.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		initcontent1();
	}

	private void initcontent1() {
		Content content1 = new Content();
		content1.title1 = "提示条款";
		content1.title2 = "";
		content1.title3 = "";
		content1.content = "\r\r\r\r\r\r\r\r欢迎您与各宅友平台经营者（详见定义条款）共同签署本《宅友平台服务协议》（下称“本协议”）并使用宅友平台服务！\n\n\r\r\r\r\r\r\r\r本协议为《宅友服务协议》修订版本，自本协议发布之日起，宅友平台各处所称“宅友服务协议”均指本协议。\n\n\r\r\r\r\r\r\r\r各服务条款前所列索引关键词仅为帮助您理解该条款表达的主旨之用，不影响或限制本协议条款的含义或解释。为维护您自身权益，建议您仔细阅读各条款具体表述。\n\n\r\r\r\r\r\r\r\r【审慎阅读】您在申请注册流程中点击同意本协议之前，应当认真阅读本协议。请您务必审慎阅读、充分理解各条款内容，特别是免除或者限制责任的条款、法律适用和争议解决条款。免除或者限制责任的条款将以粗体下划线标识，您应重点阅读。如您对协议有任何疑问，可向宅友平台客服咨询。\n\n\r\r\r\r\r\r\r\r【签约动作】当您按照注册页面提示填写信息、阅读并同意本协议且完成全部注册程序后，即表示您已充分阅读、理解并接受本协议的全部内容，并与宅友达成一致，成为宅友平台“用户”。阅读本协议的过程中，如果您不同意本协议或其中任何条款约定，您应立即停止注册程序。";

		Content content2 = new Content();
		content2.title1 = "一、 定义";
		content2.title2 = "";
		content2.title3 = "";
		content2.content = "宅友平台：指包括宅友网（域名为 zhaiyou.me）、安卓版宅友app等网站及客户端。\n\n宅友：宅友平台经营者的单称或合称，包括宅友网经营者赣州斯塔克网络技术有限公司。\n\n宅友平台服务：宅友基于互联网，以包含宅友平台网站、客户端等在内的各种形态（包括未来技术发展出现的新的服务形态）向您提供的各项服务。\n\n宅友平台规则：包括在所有宅友平台规则频道内已经发布及后续发布的全部规则、解读、公告等内容以及各平台在帮派、论坛、帮助中心内发布的各类规则、实施细则、产品流程说明、公告等。\n\n同一用户：使用同一身份认证信息或经宅友排查认定多个宅友账户的实际控制人为同一人的，均视为同一用户。";

		Content content3 = new Content();
		content3.title1 = "二、 协议范围";
		content3.title2 = "2.1 签约主体";
		content3.title3 = "1、注册资格";
		content3.content = "【平等主体】本协议由您与宅友平台经营者共同缔结，本协议对您与宅友平台经营者均具有合同效力。\n\n【主体信息】宅友平台经营者是指经营宅友平台的各法律主体，您可随时查看宅友平台各网站首页底部公示的证照信息以确定与您履约的宅友主体。本协议项下，宅友平台经营者可能根据宅友平台的业务调整而发生变更，变更后的宅友平台经营者与您共同履行本协议并向您提供服务，宅友平台经营者的变更不会影响您本协议项下的权益。宅友平台经营者还有可能因为提供新的宅友平台服务而新增，如您使用新增的宅友平台服务的，视为您同意新增的宅友平台经营者与您共同履行本协议。发生争议时，您可根据您具体使用的服务及对您权益产生影响的具体行为对象确定与您履约的主体及争议相对方。";

		Content content4 = new Content();
		content4.title1 = "";
		content4.title2 = "2.2补充协议";
		content4.title3 = "";
		content4.content = "由于互联网高速发展，您与宅友签署的本协议列明的条款并不能完整罗列并覆盖您与宅友所有权利与义务，现有的约定也不能保证完全符合未来发展的需求。因此，宅友平台规则均为本协议的补充协议，与本协议不可分割且具有同等法律效力。如您使用宅友平台服务，视为您同意上述补充协议。";

		Content content5 = new Content();
		content5.title1 = "三、 账户注册与使用";
		content5.title2 = "3.1 用户资格";
		content5.title3 = "";
		content5.content = "您确认，在您开始注册程序使用宅友平台服务前，您应当具备中华人民共和国法律规定的与您行为相适应的民事行为能力。若您不具备前述与您行为相适应的民事行为能力，则您及您的监护人应依照法律规定承担因此而导致的一切后果。";

		Content content6 = new Content();
		content6.title1 = "";
		content6.title2 = "3.2 账户说明";
		content6.title3 = "";
		content6.content = "【账户获得】当您按照注册页面提示填写信息、阅读并同意本协议且完成全部注册程序后，您可获得宅友平台账户并成为宅友平台用户。\n宅友平台只允许每位用户使用一个宅友平台账户。如有证据证明或宅友平台有理由相信您存在不当注册或不当使用多个宅友平台账户的情形，宅友平台可采取冻结或关闭账户、取消订单、拒绝提供服务等措施，如给宅友平台及相关方造成损失的，您还应承担赔偿责任。\n\n【账户使用】您有权使用您设置或确认的宅友会员名、邮箱、手机号码（以下简称“账户名称”）及您设置的密码（账户名称及密码合称“账户”）登录宅友平台。\n由于您的宅友账户关联您的个人信息及宅友平台商业信息，您的宅友账户仅限您本人使用。未经宅友平台同意，您直接或间接授权第三方使用您宅友账户或获取您账户项下信息的行为无效。如宅友平台判断您宅友账户的使用可能危及您的账户安全及/或宅友平台信息安全的，宅友平台可拒绝提供相应服务或终止本协议。\n\n【账户转让】由于用户账户关联用户信用信息，仅当有法律明文规定、司法裁定或经宅友同意，并符合宅友平台规则规定的用户账户转让流程的情况下，您可进行账户的转让。您的账户一经转让，该账户项下权利义务一并转移。除此外，您的账户不得以任何方式转让，否则宅友平台有权追究您的违约责任，且由此产生的一切责任均由您承担。\n\n【实名认证】作为宅友平台经营者，为使您更好地使用宅友平台的各项服务，保障您的账户安全，宅友可要求您按要求及我国法律规定完成实名认证。\n\n【不活跃账户回收】如您的账户同时符合以下条件，则宅友可回收您的账户，您的账户将不能再登录任一宅友平台，相应服务同时终止：\n（一）连续六个月未用于登录任一宅友平台；\n（二）不存在未到期的有效业务。";

		Content content7 = new Content();
		content7.title1 = "";
		content7.title2 = "3.3 注册信息管理";
		content7.title3 = "3.3.1 真实合法";
		content7.content = "【信息真实】在使用宅友平台服务时，您应当按宅友平台页面的提示准确完整地提供您的信息（包括您的姓名及电子邮件地址、联系电话、联系地址等），以便宅友或其他用户与您联系。您了解并同意，您有义务保持您提供信息的真实性及有效性。\n\n【会员名的合法性】您设置的宅友会员名不得违反国家法律法规及宅友网规则关于会员名的管理规定，否则宅友可回收您的宅友会员名。宅友会员名的回收不影响您以邮箱、手机号码登录宅友平台并使用宅友平台服务。";

		Content content8 = new Content();
		content8.title1 = "";
		content8.title2 = "";
		content8.title3 = "3.3.2 更新维护";
		content8.content = "您应当及时更新您提供的信息，在法律有明确规定要求宅友作为平台服务提供者必须对部分用户（如平台卖家等）的信息进行核实的情况下，宅友将依法不时地对您的信息进行检查核实，您应当配合提供最新、真实、完整、有效的信息。\n如宅友按您最后一次提供的信息与您联系未果、您未按宅友的要求及时提供信息、您提供的信息存在明显不实或行政司法机关核实您提供的信息无效的，您将承担因此对您自身、他人及宅友造成的全部损失与不利后果。宅友可向您发出询问或要求整改的通知，并要求您进行重新认证，直至中止、终止对您提供部分或全部宅友平台服务，宅友对此不承担责任。";

		Content content9 = new Content();
		content9.title1 = "";
		content9.title2 = "";
		content9.title3 = "";
		content9.content = "";

		Content content10 = new Content();
		content10.title1 = "";
		content10.title2 = "3.4 账户安全规范";
		content10.title3 = "";
		content10.content = "【账户安全保管义务】您的账户为您自行设置并由您保管，宅友任何时候均不会主动要求您提供您的账户密码。因此，建议您务必保管好您的账户， 并确保您在每个上网时段结束时退出登录并以正确步骤离开宅友平台。\n账户因您主动泄露或因您遭受他人攻击、诈骗等行为导致的损失及后果，宅友并不承担责任，您应通过司法、行政等救济途径向侵权行为人追偿。\n\n【账户行为责任自负】除宅友存在过错外，您应对您账户项下的所有行为结果（包括但不限于在线签署各类协议、发布信息、购买商品及服务及披露信息等）负责。\n\n【日常维护须知】如发现任何未经授权使用您账户登录宅友平台或其他可能导致您账户遭窃、遗失的情况，建议您立即通知宅友，并授权宅友将该信息同步宅友平台。您理解宅友对您的任何请求采取行动均需要合理时间，且宅友应您请求而采取的行动可能无法避免或阻止侵害后果的形成或扩大，除宅友存在法定过错外，宅友不承担责任。";

		Content content11 = new Content();
		content11.title1 = "四、 宅友平台服务及规范";
		content11.title2 = "";
		content11.title3 = "";
		content11.content = "【服务概况】您有权在宅友平台上享受店铺管理、商品及/或服务的销售与推广、商品及/或服务的购买与评价、交易争议处理等服务。宅友提供的服务内容众多，具体您可登录宅友平台浏览。";

		Content content12 = new Content();
		content12.title1 = "";
		content12.title2 = "4.1 店铺管理";
		content12.title3 = "";
		content12.content = "【店铺创建】通过在宅友网创建店铺，您可发布全新或二手商品及/或服务信息并与其他用户达成交易。基于宅友网管理需要，您理解并认可，同一用户在宅友网仅能开设一家店铺，宅友可关闭您在宅友网同时开设的其他店铺。\n\n【店铺转让】由于店铺与账户的不可分性，店铺转让实质为店铺经营者账户的转让，店铺转让的相关要求与限制请适用本协议3.2条账户转让条款。\n\n【店铺关停】如您通过下架全部商品短暂关停您的店铺，您应当对您店铺关停前已达成的交易继续承担发货、退换货及质保维修、维权投诉处理等交易保障责任。\n在您的店铺连续六周无出售中的商品的情况下，宅友也可依据宅友网规则关停您的店铺。\n依据上述约定关停店铺均不会影响您已经累积的信用。";

		Content content13 = new Content();
		content13.title1 = "";
		content13.title2 = "4.2商品及/或服务的销售与推广";
		content13.title3 = "";
		content13.content = "【商品及/或服务信息发布】通过宅友提供的服务，您有权通过文字、图片、视频、音频等形式在宅友平台上发布商品及/或服务信息、招揽和物色交易对象、达成交易。\n\n【禁止销售范围】您应当确保您对您在宅友平台上发布的商品及/或服务享有相应的权利，您不得在宅友平台上销售以下商品及/或提供以下服务：\n（一）国家禁止或限制的；\n（二）侵犯他人知识产权或其它合法权益的；\n（三）宅友平台规则、公告、通知或各平台与您单独签署的协议中已明确说明不适合在宅友平台上销售及/或提供的。\n\n【交易秩序保障】您应当遵守诚实信用原则，确保您所发布的商品及/或服务信息真实、与您实际所销售的商品及/或提供的服务相符，并在交易过程中切实履行您的交易承诺。\n您应当维护宅友平台市场良性竞争秩序，不得贬低、诋毁竞争对手，不得干扰宅友平台上进行的任何交易、活动，不得以任何不正当方式提升或试图提升自身的信用度，不得以任何方式干扰或试图干扰宅友平台的正常运作。\n\n【促销及推广】您有权自行决定商品及/或服务的促销及推广方式，宅友亦为您提供了形式丰富的促销推广工具。您的促销推广行为应当符合国家相关法律法规及宅友平台的要求。\n\n【依法纳税】依法纳税是每一个公民、企业应尽的义务，您应对销售额/营业额超过法定免征额部分及时、足额地向税务主管机关申报纳税。";

		Content content14 = new Content();
		content14.title1 = "";
		content14.title2 = "4.3商品及/或服务的购买与评价";
		content14.title3 = "";
		content14.content = "【商品及/或服务的购买】当您在宅友平台购买商品及/或服务时，请您务必仔细确认所购商品的品名、价格、数量、型号、规格、尺寸或服务的时间、内容、限制性要求等重要事项，并在下单时核实您的联系地址、电话、收货人等信息。如您填写的收货人非您本人，则该收货人的行为和意思表示产生的法律后果均由您承担。\n您的购买行为应当基于真实的消费需求，不得存在对商品及/或服务实施恶意购买、恶意维权等扰乱宅友平台正常交易秩序的行为。基于维护宅友平台交易秩序及交易安全的需要，宅友发现上述情形时可主动执行关闭相关交易订单等操作。\n\n【评价】您有权在宅友平台提供的评价系统中对与您达成交易的其他用户商品及/或服务进行评价。您应当理解，您在宅友平台的评价信息是公开的，如您不愿意在评价信息中向公众披露您的身份信息，您有权选择通过匿名形式发表评价内容。\n您的所有评价行为应遵守宅友平台规则的相关规定，评价内容应当客观真实，不应包含任何污言秽语、色情低俗、广告信息及法律法规与本协议列明的其他禁止性信息；您不应以不正当方式帮助他人提升信用或利用评价权利对其他用户实施威胁、敲诈勒索。宅友可按照宅友平台规则的相关规定对您实施上述行为所产生的评价信息进行删除或屏蔽。";

		Content content15 = new Content();
		content15.title1 = "";
		content15.title2 = "4.4交易争议处理";
		content15.title3 = "";
		content15.content = "【交易争议处理途径】您在宅友平台交易过程中与其他用户发生争议的，您或其他用户中任何一方均有权选择以下途径解决：\n（一）与争议相对方自主协商;\n（二）使用宅友平台提供的争议调处服务；\n（三）请求消费者协会或者其他依法成立的调解组织调解；\n（四）向有关行政部门投诉；\n（五）根据与争议相对方达成的仲裁协议（如有）提请仲裁机构仲裁；\n（六）向人民法院提起诉讼。\n\n【平台调处服务】如您依据宅友平台规则使用使用宅友平台的争议调处服务，则表示您认可并愿意履行宅友平台的客服或大众评审员（“调处方”）作为独立的第三方根据其所了解到的争议事实并依据宅友平台规则所作出的调处决定（包括调整相关订单的交易状态、判定将争议款项的全部或部分支付给交易一方或双方等）。在宅友平台调处决定作出前，您可选择上述（三）、（四）、（五）、（六）途径（下称“其他争议处理途径”）解决争议以中止宅友平台的争议调处服务。\n如您对调处决定不满意，您仍有权采取其他争议处理途径解决争议，但通过其他争议处理途径未取得终局决定前，您仍应先履行调处决定。";

		Content content16 = new Content();
		content16.title1 = "";
		content16.title2 = "4.5费用";
		content16.title3 = "";
		content16.content = "宅友为宅友平台向您提供的服务付出了大量的成本，除宅友平台明示的收费业务外，宅友向您提供的服务目前是免费的。如未来宅友向您收取合理费用，宅友会采取合理途径并以足够合理的期限提前通过法定程序并以本协议第八条约定的方式通知您，确保您有充分选择的权利。";

		Content content17 = new Content();
		content17.title1 = "";
		content17.title2 = "4.6责任限制";
		content17.title3 = "";
		content17.content = "【不可抗力及第三方原因】宅友依照法律规定履行基础保障义务，但对于下述原因导致的合同履行障碍、履行瑕疵、履行延后或履行内容变更等情形，宅友并不承担相应的违约责任：\n（一）因自然灾害、罢工、暴乱、战争、政府行为、司法行政命令等不可抗力因素；\n（二）因电力供应故障、通讯网络故障等公共服务因素或第三人因素；\n（三）在宅友已尽善意管理的情况下，因常规或紧急的设备与系统维护、设备与系统故障、网络信息与数据安全等因素。\n\n【海量信息】宅友仅向您提供宅友平台服务，您了解宅友平台上的信息系用户自行发布，且可能存在风险和瑕疵。鉴于宅友平台具备存在海量信息及信息网络环境下信息与实物相分离的特点，宅友无法逐一审查商品及/或服务的信息，无法逐一审查交易所涉及的商品及/或服务的质量、安全以及合法性、真实性、准确性，对此您应谨慎判断。\n\n【调处决定】您理解并同意，在争议调处服务中，宅友平台的客服、大众评审员并非专业人士，仅能以普通人的认知对用户提交的凭证进行判断。因此，除存在故意或重大过失外，调处方对争议调处决定免责。";

		Content content18 = new Content();
		content18.title1 = "五、 用户信息的保护及授权";
		content18.title2 = "5.1个人信息的保护";
		content18.title3 = "";
		content18.content = "宅友非常重视用户个人信息（即能够独立或与其他信息结合后识别用户身份的信息）的保护，在您使用宅友提供的服务时，您同意宅友按照在宅友平台上公布的隐私权政策收集、存储、使用、披露和保护您的个人信息。";

		Content content19 = new Content();
		content19.title1 = "";
		content19.title2 = "5.2非个人信息的保证与授权";
		content19.title3 = "";
		content19.content = "【信息的发布】您声明并保证，您对您所发布的信息拥有相应、合法的权利。否则，宅友可对您发布的信息依法或依本协议进行删除或屏蔽。\n\n【禁止性信息】您应当确保您所发布的信息不包含以下内容：\n（一）违反国家法律法规禁止性规定的；\n（二）政治宣传、封建迷信、淫秽、色情、赌博、暴力、恐怖或者教唆犯罪的；\n（三）欺诈、虚假、不准确或存在误导性的；\n（四）侵犯他人知识产权或涉及第三方商业秘密及其他专有权利的；\n（五）侮辱、诽谤、恐吓、涉及他人隐私等侵害他人合法权益的；\n（六）存在可能破坏、篡改、删除、影响宅友平台任何系统正常运行或未经授权秘密获取宅友平台及其他用户的数据、个人资料的病毒、木马、爬虫等恶意软件、程序代码的；\n（七）其他违背社会公共利益或公共道德或依据相关宅友平台协议、规则的规定不适合在宅友平台上发布的。\n\n【授权使用】对于您提供及发布除个人信息外的文字、图片、视频、音频等非个人信息，在版权保护期内您免费授予宅友及其关联公司获得全球排他的许可使用权利及再授权给其他第三方使用的权利。您同意宅友及其关联公司存储、使用、复制、修订、编辑、发布、展示、翻译、分发您的非个人信息或制作其派生作品，并以已知或日后开发的形式、媒体或技术将上述信息纳入其它作品内。\n为方便您使用宅友平台等其他相关服务，您授权宅友将您在账户注册和使用宅友平台服务过程中提供、形成的信息传递给宅友平台等其他相关服务提供者，或从宅友平台等其他相关服务提供者获取您在注册、使用相关服务期间提供、形成的信息。";

		Content content20 = new Content();
		content20.title1 = "六、 用户的违约及处理";
		content20.title2 = "6.1 违约认定";
		content20.title3 = "";
		content20.content = "发生如下情形之一的，视为您违约：\n（一）使用宅友平台服务时违反有关法律法规规定的；\n（二）违反本协议或本协议补充协议（即本协议第2.2条）约定的。\n为适应电子商务发展和满足海量用户对高效优质服务的需求，您理解并同意，宅友可在宅友平台规则中约定违约认定的程序和标准。如：宅友可依据您的用户数据与海量用户数据的关系来认定您是否构成违约；您有义务对您的数据异常现象进行充分举证和合理解释，否则将被认定为违约。";

		Content content21 = new Content();
		content21.title1 = "";
		content21.title2 = "6.2 违约处理措施";
		content21.title3 = "";
		content21.content = "【信息处理】您在宅友平台上发布的信息构成违约的，宅友可根据相应规则立即对相应信息进行删除、屏蔽处理或对您的商品进行下架、监管。\n\n【行为限制】您在宅友平台上实施的行为，或虽未在宅友平台上实施但对宅友平台及其用户产生影响的行为构成违约的，宅友可依据相应规则对您执行账户扣分、限制参加营销活动、中止向您提供部分或全部服务、划扣违约金等处理措施。如您的行为构成根本违约的，宅友可查封您的账户，终止向您提供服务。\n\n【处理结果公示】宅友可将对您上述违约行为处理措施信息以及其他经国家行政或司法机关生效法律文书确认的违法信息在宅友平台上予以公示。";

		Content content22 = new Content();
		content22.title1 = "";
		content22.title2 = "6.3赔偿责任";
		content22.title3 = "";
		content22.content = "如您的行为使宅友及/或其关联公司遭受损失（包括自身的直接经济损失、商誉损失及对外支付的赔偿金、和解款、律师费、诉讼费等间接经济损失），您应赔偿宅友及/或其关联公司的上述全部损失。\n如您的行为使宅友及/或其关联公司遭受第三人主张权利，宅友及/或其关联公司可在对第三人承担金钱给付等义务后就全部损失向您追偿。\n如因您的行为使得第三人遭受损失或您怠于履行调处决定、宅友及/或其关联公司出于社会公共利益保护或消费者权益保护目的，可自您的账户中划扣相应款项进行支付。如您的账户余额或保证金不足以支付相应款项的，您同意委托宅友使用自有资金代您支付上述款项，您应当返还该部分费用并赔偿因此造成宅友的全部损失。\n您同意宅友自您的账户中划扣相应款项支付上述赔偿款项。如您账户中的款项不足以支付上述赔偿款项的，宅友及/或关联公司可直接抵减您在宅友及/或其关联公司其它协议项下的权益，并可继续追偿。";

		Content content23 = new Content();
		content23.title1 = "";
		content23.title2 = "6.4特别约定";
		content23.title3 = "";
		content23.content = "【商业贿赂】如您向宅友及/或其关联公司的雇员或顾问等提供实物、现金、现金等价物、劳务、旅游等价值明显超出正常商务洽谈范畴的利益，则可视为您存在商业贿赂行为。发生上述情形的，宅友可立即终止与您的所有合作并向您收取违约金及/或赔偿金，该等金额以宅友因您的贿赂行为而遭受的经济损失和商誉损失作为计算依据。\n\n【关联处理】如您因严重违约导致宅友终止本协议时，出于维护平台秩序及保护消费者权益的目的，宅友及/或其关联公司可对与您在其他协议项下的合作采取中止甚或终止协议的措施，并以本协议第八条约定的方式通知您。\n如宅友与您签署的其他协议及宅友及/或其关联公司与您签署的协议中明确约定了对您在本协议项下合作进行关联处理的情形，则宅友出于维护平台秩序及保护消费者权益的目的，可在收到指令时中止甚至终止协议，并以本协议第八条约定的方式通知您。";

		Content content24 = new Content();
		content24.title1 = "七、 协议的变更";
		content24.title2 = "";
		content24.title3 = "";
		content24.content = "宅友可根据国家法律法规变化及维护交易秩序、保护消费者权益需要，不时修改本协议、补充协议，变更后的协议、补充协议（下称“变更事项”）将通过法定程序并以本协议第八条约定的方式通知您。\n如您不同意变更事项，您有权于变更事项确定的生效日前联系宅友反馈意见。如反馈意见得以采纳，宅友将酌情调整变更事项。\n如您对已生效的变更事项仍不同意的，您应当于变更事项确定的生效之日起停止使用宅友平台服务，变更事项对您不产生效力；如您在变更事项生效后仍继续使用宅友平台服务，则视为您同意已生效的变更事项。";

		Content content25 = new Content();
		content25.title1 = "八、 通知";
		content25.title2 = "8.1有效联系方式";
		content25.title3 = "";
		content25.content = "您在注册成为宅友平台用户，并接受宅友平台服务时，您应该向宅友提供真实有效的联系方式（包括您的电子邮件地址、联系电话、联系地址等），对于联系方式发生变更的，您有义务及时更新有关信息，并保持可被联系的状态。\n您在注册宅友平台用户时生成的用于登陆宅友平台接收站内信、系统消息和宅友旺旺即时信息的会员账号（包括子账号），也作为您的有效联系方式。\n宅友将向您的上述联系方式的其中之一或其中若干向您送达各类通知，而此类通知的内容可能对您的权利义务产生重大的有利或不利影响，请您务必及时关注。\n您有权通过您注册时填写的手机号码或者电子邮箱获取您感兴趣的商品广告信息、促销优惠等商业性信息；您如果不愿意接收此类信息，您有权通过宅友提供的相应的退订功能进行退订。";

		Content content26 = new Content();
		content26.title1 = "";
		content26.title2 = "8.2 通知的送达";
		content26.title3 = "";
		content26.content = "宅友通过上述联系方式向您发出通知，其中以电子的方式发出的书面通知，包括但不限于在宅友平台公告，向您提供的联系电话发送手机短信，向您提供的电子邮件地址发送电子邮件，向您的账号发送旺旺信息、系统消息以及站内信信息，在发送成功后即视为送达；以纸质载体发出的书面通知，按照提供联系地址交邮后的第五个自然日即视为送达。\n对于在宅友平台上因交易活动引起的任何纠纷，您同意司法机关（包括但不限于人民法院）可以通过手机短信、电子邮件或宅友旺旺等现代通讯方式或邮寄方式向您送达法律文书（包括但不限于诉讼文书）。您指定接收法律文书的手机号码、电子邮箱或宅友账号等联系方式为您在宅友平台注册、更新时提供的手机号码、电子邮箱联系方式以及在注册宅友用户时生成宅友账号，司法机关向上述联系方式发出法律文书即视为送达。您指定的邮寄地址为您的法定联系地址或您提供的有效联系地址。\n您同意司法机关可采取以上一种或多种送达方式向您达法律文书，司法机关采取多种方式向您送达法律文书，送达时间以上述送达方式中最先送达的为准。\n您同意上述送达方式适用于各个司法程序阶段。如进入诉讼程序的，包括但不限于一审、二审、再审、执行以及督促程序等。\n你应当保证所提供的联系方式是准确、有效的，并进行实时更新。如果因提供的联系方式不确切，或不及时告知变更后的联系方式，使法律文书无法送达或未及时送达，由您自行承担由此可能产生的法律后果。";

		Content content27 = new Content();
		content27.title1 = "九、 协议的终止";
		content27.title2 = "9.1 终止的情形";
		content27.title3 = "";
		content27.content = "【用户发起的终止】您有权通过以下任一方式终止本协议：\n（一）在满足宅友网公示的账户注销条件时您通过网站自助服务注销您的账户的；\n（二）变更事项生效前您停止使用并明示不愿接受变更事项的；\n（三）您明示不愿继续使用宅友平台服务，且符合宅友网终止条件的。\n\n【宅友发起的终止】出现以下情况时，宅友可以本协议第八条的所列的方式通知您终止本协议：\n（一）您违反本协议约定，宅友依据违约条款终止本协议的；\n（二）您盗用他人账户、发布违禁信息、骗取他人财物、售假、扰乱市场秩序、采取不正当手段谋利等行为，宅友依据宅友平台规则对您的账户予以查封的；\n（三）除上述情形外，因您多次违反宅友平台规则相关规定且情节严重，宅友依据宅友平台规则对您的账户予以查封的；\n（四）您的账户被宅友依据本协议回收的；\n（五）您在宅友平台有欺诈、发布或销售假冒伪劣/侵权商品、侵犯他人合法权益或其他严重违法违约行为的；\n（六）其它应当终止服务的情况。";

		Content content28 = new Content();
		content28.title1 = "";
		content28.title2 = "9.2 协议终止后的处理";
		content28.title3 = "";
		content28.content = "【用户信息披露】本协议终止后，除法律有明确规定外，宅友无义务向您或您指定的第三方披露您账户中的任何信息。\n\n【宅友权利】本协议终止后，宅友仍享有下列权利：\n（一）继续保存您留存于宅友平台的本协议第五条所列的各类信息；\n（二）对于您过往的违约行为，宅友仍可依据本协议向您追究违约责任。\n\n【交易处理】本协议终止后，对于您在本协议存续期间产生的交易订单，宅友可通知交易相对方并根据交易相对方的意愿决定是否关闭该等交易订单；如交易相对方要求继续履行的，则您应当就该等交易订单继续履行本协议及交易订单的约定，并承担因此产生的任何损失或增加的任何费用。";

		Content content29 = new Content();
		content29.title1 = "十、 法律适用、管辖与其他";
		content29.title2 = "";
		content29.title3 = "";
		content29.content = "【法律适用】本协议之订立、生效、解释、修订、补充、终止、执行与争议解决均适用中华人民共和国大陆地区法律；如法律无相关规定的，参照商业惯例及/或行业惯例。\n\n【管辖】您因使用宅友平台服务所产生及与宅友平台服务有关的争议，由宅友与您协商解决。协商不成时，任何一方均可向被告所在地人民法院提起诉讼。\n\n【可分性】本协议任一条款被视为废止、无效或不可执行，该条应视为可分的且并不影响本协议其余条款的有效性及可执行性。";

		Content content30 = new Content();
		content30.title1 = "";
		content30.title2 = "";
		content30.title3 = "";
		content30.content = "";

		Content content31 = new Content();
		content31.title1 = "";
		content31.title2 = "";
		content31.title3 = "";
		content31.content = "";

		Content content32 = new Content();
		content32.title1 = "";
		content32.title2 = "";
		content32.title3 = "";
		content32.content = "";

		Content content33 = new Content();
		content33.title1 = "";
		content33.title2 = "";
		content33.title3 = "";
		content33.content = "";

		allContent.add(content1);
		allContent.add(content2);
		allContent.add(content3);
		allContent.add(content4);
		allContent.add(content5);
		allContent.add(content6);
		allContent.add(content7);
		allContent.add(content8);
		allContent.add(content9);
		allContent.add(content10);
		allContent.add(content11);
		allContent.add(content12);
		allContent.add(content13);
		allContent.add(content14);
		allContent.add(content15);
		allContent.add(content16);
		allContent.add(content17);
		allContent.add(content18);
		allContent.add(content19);
		allContent.add(content20);
		allContent.add(content21);
		allContent.add(content22);
		allContent.add(content23);
		allContent.add(content24);
		allContent.add(content25);
		allContent.add(content26);
		allContent.add(content27);
		allContent.add(content28);
		allContent.add(content29);

		act_useragreementdetail_listview.setAdapter(new Adapter());
	}

	class Content {
		String title1;
		String title2;
		String title3;
		String content;
	}

	/**
	 * 适配器
	 */
	class Adapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return allContent.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return allContent.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(UserAgreementActivity.this)
						.inflate(R.layout.item_useragreementdetail, null);
				holder.title1 = (TextView) convertView
						.findViewById(R.id.title1);
				holder.title2 = (TextView) convertView
						.findViewById(R.id.title2);
				holder.title3 = (TextView) convertView
						.findViewById(R.id.title3);
				holder.content = (TextView) convertView
						.findViewById(R.id.content);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Content content = allContent.get(position);
			if (TextUtils.isEmpty(content.title1)) {
				holder.title1.setVisibility(View.GONE);
			} else {
				holder.title1.setVisibility(View.VISIBLE);
				holder.title1.setText(content.title1);
			}

			if (TextUtils.isEmpty(content.title2)) {
				holder.title2.setVisibility(View.GONE);
			} else {
				holder.title2.setVisibility(View.VISIBLE);
				holder.title2.setText(content.title2);
			}

			if (TextUtils.isEmpty(content.title3)) {
				holder.title3.setVisibility(View.GONE);
			} else {
				holder.title3.setVisibility(View.VISIBLE);
				holder.title3.setText(content.title3);
			}

			if (TextUtils.isEmpty(content.content)) {
				holder.content.setVisibility(View.GONE);
			} else {
				holder.content.setVisibility(View.VISIBLE);
				holder.content.setText(content.content);
			}

			return convertView;
		}

		class ViewHolder {
			TextView title1;
			TextView title2;
			TextView title3;
			TextView content;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}
}
