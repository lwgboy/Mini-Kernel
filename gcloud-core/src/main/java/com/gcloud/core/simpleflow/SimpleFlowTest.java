package com.gcloud.core.simpleflow;

public class SimpleFlowTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SimpleFlowChain<String,String> chain=new SimpleFlowChain<String,String>();
		chain.name("test").then(new Flow<String>("1111") {

			@Override
			public void run(SimpleFlowChain chain, String data) {
				// TODO Auto-generated method stub
				data="step1";
				System.out.println(data);
				chain.next();
			}

			@Override
			public void rollback(SimpleFlowChain chain, String data) {
				// TODO Auto-generated method stub
				System.out.println("step1 rollback");
				chain.rollback();
			}
		}).then(new NoRollbackFlow<Object>("22222") {

			@Override
			public void run(SimpleFlowChain chain, Object data) {
				// TODO Auto-generated method stub
				data="step2";
				System.out.println(data);
				chain.next();
			}
		}).then(new Flow<String>("3333") {

			@Override
			public void run(SimpleFlowChain chain, String data) {
				// TODO Auto-generated method stub
				data="step3";
				System.out.println(data);
				chain.fail("dfdfdfd");
			}

			@Override
			public void rollback(SimpleFlowChain chain, String data) {
				// TODO Auto-generated method stub
				System.out.println("step 3 rollback");
				chain.rollback();
			}
		}).done(new FlowDoneHandler<String>() {

			@Override
			public void handle(String data) {
				// TODO Auto-generated method stub
				System.out.println("done");
			}
		}).flowFinally(new FlowFinallyHandler() {
			
			@Override
			public void handle() {
				// TODO Auto-generated method stub
				System.out.println("finally");
			}
		}).start();
	}

}
